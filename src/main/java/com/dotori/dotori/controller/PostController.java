package com.dotori.dotori.controller;

import com.dotori.dotori.dto.PageRequestDTO;
import com.dotori.dotori.dto.PageResponseDTO;
import com.dotori.dotori.dto.PostDTO;
import com.dotori.dotori.dto.PostListCommentCountDTO;
import com.dotori.dotori.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<PostListCommentCountDTO> responseDTO = postService.listWithCommentCount(pageRequestDTO);
        log.info(responseDTO);
        model.addAttribute("responseDTO", responseDTO);
    }

    @GetMapping("/register")
    public void registerGet(Model model) {
        // + 버튼을 누르면 글 등록 페이지로 들어감
    }
    @PostMapping("/register")
    public String registerPost(@Valid PostDTO postDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, MultipartFile file) throws Exception {
        if (bindingResult.hasErrors()) {
            log.error("has Error");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/post/register";
        }

        log.info("Post register" + postDTO.getContent());
        int pid = postService.addPost(postDTO, file);
        redirectAttributes.addFlashAttribute("result", pid);

        return "redirect:/post/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(int pid, PageRequestDTO pageRequestDTO, Model model) {
        PostDTO postDTO = postService.getPost(pid);
        log.info(postDTO);
        model.addAttribute("dto", postDTO);
    }

    @PostMapping("/modify")
    public String modify(PageRequestDTO pageRequestDTO, @Valid PostDTO postDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, MultipartFile file) throws Exception {
        log.info("Post Modify coming");
        if (bindingResult.hasErrors()) {
            log.error("has Error");
            String link = pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("pid", postDTO.getPid());
            return "redirect:/post/modify?" + link;
        }
        postService.modifyPost(postDTO, file);
        redirectAttributes.addFlashAttribute("result", "modify success");
        redirectAttributes.addAttribute("pid", postDTO.getPid());

        return "redirect:/post/read";
    }

    @PostMapping("/remove")
    public String remove(PostDTO postDTO,PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes) {

        log.info("remove : " + postDTO.getPid());
        postService.deletePost(postDTO.getPid());

        //삭제시에는 페이지 번호를 1로, 사이즈는 전달 받는다.
        redirectAttributes.addAttribute("page", 1);
        redirectAttributes.addAttribute("size", pageRequestDTO.getSize());
        return "redirect:/post/list";
    }
}
