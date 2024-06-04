package com.dotori.dotori.controller;

import com.dotori.dotori.dto.*;
import com.dotori.dotori.service.AuthService;
import com.dotori.dotori.service.PostService;
import com.dotori.dotori.service.PostServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final PostServiceImpl postServiceImpl;
    private final AuthService authService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/list")
    public String list(PageRequestDTO pageRequestDTO, Model model, @AuthenticationPrincipal AuthSecurityDTO authSecurityDTO) {
        PageResponseDTO<PostDTO> responseDTO = postService.listWithCommentCount(pageRequestDTO);

        if (!authSecurityDTO.isTutorialDone()) {
            return "redirect:/todo/list";
        }

        log.info(responseDTO);

        int totalPosts = responseDTO.getTotal();
        if (totalPosts > 0) { // 게시글이 있는 경우
            List<PostDTO> postDTOList = responseDTO.getPostLists();
            if (authSecurityDTO != null) {
                int aid = authSecurityDTO.getAid();

                for (PostDTO postDTO : postDTOList) {
                    // postDTO가 null인지 확인
                    if (postDTO != null) {
                        // 현재 사용자가 해당 게시물에 좋아요를 눌렀는지 확인
                        boolean isLiked = postService.isLikedByUser(postDTO.getPid(), aid);

                        // PostDTO의 liked 필드에 좋아요 상태를 설정
                        postDTO.setLiked(isLiked);
                    } else {
                        log.warn("Null PostDTO encountered in the list.");
                    }
                }
            }
        } else {
            log.info("No posts found."); // 게시글이 없는 경우
        }

        model.addAttribute("responseDTO", responseDTO);

        return "post/list";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/register")
    public String registerGet(@AuthenticationPrincipal AuthSecurityDTO authSecurityDTO, Model model) {
        // + 버튼을 누르면 글 등록 페이지로 들어감
        if (!authSecurityDTO.isTutorialDone()) {
            return "redirect:/todo/list";
        }
        return "post/register";
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/register")
    public String registerPost(@Valid PostDTO postDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                               @RequestParam(value = "files", required = false) List<MultipartFile> files) throws Exception {
        if (bindingResult.hasErrors()) {
            log.error("has Error");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/post/register";
        }

        log.info("Post register" + postDTO.getContent());
        int pid = postService.addPost(postDTO, files);
        redirectAttributes.addFlashAttribute("result", pid);

        return "redirect:/post/list";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/read")
    public void read(int pid, PageRequestDTO pageRequestDTO, Model model, @AuthenticationPrincipal AuthSecurityDTO authSecurityDTO) {
        PostDTO postDTO = postService.getPost(pid);
        log.info(postDTO);
        int likes = postService.countLikes(pid);

        if (authSecurityDTO != null) {
            int aid = authSecurityDTO.getAid();

            // 현재 사용자가 해당 게시물에 좋아요를 눌렀는지 확인
            boolean isLiked = postService.isLikedByUser(pid, aid);

            // PostDTO에 좋아요 상태를 설정
            postDTO.setLiked(isLiked);
        }

        model.addAttribute("dto", postDTO);
        model.addAttribute("likes", likes); // 좋아요 개수
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/modify")
    public String modifyGet(@AuthenticationPrincipal AuthSecurityDTO authSecurityDTO, PostDTO postDTO, PageRequestDTO pageRequestDTO, Model model) {
        int pid = postDTO.getPid();
        PostDTO dto = postService.getPost(pid);

        log.info(dto);

        model.addAttribute("dto", dto);

        if (authSecurityDTO != null && authSecurityDTO.getAid() == dto.getAid()) {
            // 작성자가 일치하는 경우에 대한 처리 로직 추가
            return "post/modify";
        } else {
            log.info(authSecurityDTO.getNickName());
            log.info(dto.getNickName());
            // 작성자가 일치하지 않는 경우에 대한 처리 로직 추가 (예: 예외 처리 또는 에러 페이지로 리다이렉트)
            model.addAttribute("errorMessage", "접근 권한이 없습니다."); // 에러 메시지를 모델에 추가
            return "post/read"; // 에러 페이지로 리다이렉트
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/modify")
    public String modify(PageRequestDTO pageRequestDTO, @Valid PostDTO postDTO, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         @RequestParam(value = "files", required = false) List<MultipartFile> files,
                         @RequestParam(value = "deletedThumbnails", required = false) List<String> deletedThumbnails) {
        log.info("Post Modify coming");

        try {
            if (bindingResult.hasErrors()) {
                log.error("has Error");
                String link = pageRequestDTO.getLink();
                redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
                redirectAttributes.addAttribute("pid", postDTO.getPid());
                return "redirect:/post/modify?" + link;
            }
            postService.modifyPost(postDTO, files, deletedThumbnails);

            redirectAttributes.addFlashAttribute("result", "modify success");
            redirectAttributes.addAttribute("pid", postDTO.getPid());

            return "redirect:/post/read";

        } catch (Exception e) {
            log.error("Error occurred while modifying post", e);
            redirectAttributes.addFlashAttribute("error", "게시글 수정 중 오류가 발생했습니다.");
            return "redirect:/post/modify?pid=" + postDTO.getPid();
        }

    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/remove")
    public String remove(PostDTO postDTO, PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes) {

        log.info("remove : " + postDTO.getPid());
        postService.deletePost(postDTO.getPid());

        //삭제시에는 페이지 번호를 1로, 사이즈는 전달 받는다.
        redirectAttributes.addAttribute("size", pageRequestDTO.getSize());
        return "redirect:/post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/like")
    public ResponseEntity<Map<String, Object>> likePosts(@RequestBody Map<String, Integer> requestBody) throws Exception {
        int pid = requestBody.get("pid");
        int aid = requestBody.get("aid");

        ToriBoxDTO toriBoxDTO = ToriBoxDTO.builder().pid(pid).aid(aid).build();
        int tid = postService.toriBoxPost(toriBoxDTO);

        int likeCount = postService.countLikes(pid);

        Map<String, Object> response = new HashMap<>();
        if (tid == -1) { // 좋아요 취소 시
            response.put("deleted", true);
        } else {
            response.put("tid", tid);
        }
        response.put("likeCount", likeCount);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/likes")
    public String toriBoxes(@AuthenticationPrincipal AuthSecurityDTO authSecurityDTO, Model model) {
        int aid = authSecurityDTO.getAid();
        List<PostDTO> toriBoxPosts = postService.toriBoxSelectAll(aid);

        // 각 게시글의 좋아요 카운트 설정
        for (PostDTO postDTO : toriBoxPosts) {
            int likeCount = postService.countLikes(postDTO.getPid());
            postDTO.setToriBoxCount(likeCount);
        }

        model.addAttribute("toriBoxPosts", toriBoxPosts);

        String id = authSecurityDTO.getId();
        AuthDTO authDTO = authService.info(id);

        // 현재 사용자의 좋아요 상태 확인
        for (PostDTO postDTO : toriBoxPosts) {
            boolean isLiked = postService.isLikedByUser(postDTO.getPid(), authSecurityDTO.getAid());
            postDTO.setLiked(isLiked);
        }

        // 소셜 로그인 여부 확인
        if (authDTO.isSocial() || authDTO.getProvider() != null) {
            return "post/socialLikes"; // 소셜 로그인한 회원은 post/socialLikes 뷰 반환
        } else {
            return "post/likes"; // 일반 로그인한 회원은 post/likes 뷰 반환
        }
    }

}
