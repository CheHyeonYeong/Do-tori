let count = 1; // 초기 count 값 설정

function loadMorePosts() {
    $.ajax({
        url: '/post/list',
        data: {
            page: count,
            size: 10
        },
        success: function(responseDTO) {
            const postLists = responseDTO.postLists;
            if (postLists.length > 0) {
                let html = '';
                postLists.forEach(function(post) {
                    html += `
                    <div class="card mx-auto p-2 vstack gap-3" style="width: 50%; margin: 50px">
                        <div class="card-header">
                            <img src="/assets/Profile.png" class="profile" id="profileImage" style="width: 30px; height: auto">
                            <div class="card-title" style="display:inline; font-weight: bold">${post.nickName}</div>
                            ${post.nickName === authNickName ? `
                            <div class="dropdown" style="float: right; margin-top: 3px; display: inline">
                                <a id="modifyDropdown" role="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    <svg width="5" height="20" viewBox="0 0 10 45" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <circle cx="5" cy="5" r="5" fill="black"/>
                                        <circle cx="5" cy="22" r="5" fill="black"/>
                                        <circle cx="5" cy="40" r="5" fill="black"/>
                                    </svg>
                                </a>
                                <div class="dropdown-menu dropdown-menu-end" id="mypage" aria-labelledby="navbarDropdown">
                                    <a class="dropdown-item" href="/post/modify?pid=${post.pid}">수정하기</a>
                                    <div class="dropdown-divider"></div>
                                    <div class="dropdown-item">
                                        <form th:action="@{/post/remove}" method="post">
                                            <input type="text" name="pid" value="${post.pid}" readonly style="display: none;">
                                            <button type="submit" class="removeBtn" style="all: unset; cursor: pointer;">삭제하기</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                            ` : ''}
                        </div>
                        <a href="/post/read?pid=${post.pid}&${responseDTO.link}" style="all: unset; cursor: pointer;">
                            <div class="card-body">
                                <div class="input-group mb-3">
                                    <input type="hidden" class="form-control" value="${post.pid}" readonly>
                                </div>
                                <div class="input-group mb-3" style="display: block; text-align: center; cursor: pointer">
                                    <div class="body-content" style="text-align: left">${post.content}</div>
                                    ${post.thumbnail ? `<img src="/images/${post.thumbnail}" class="img-fluid">` : ''}
                                </div>
                            </div>
                        </a>
                        <div style="margin: 0 auto; width: 95%">
                            <hr>
                            <p class="hstack gap-3 date">
                                <span>${new Date(post.regDate).toLocaleDateString()} 작성</span>
                                <span>/</span>
                                <span>
                                    ${post.modDate ? new Date(post.modDate).toLocaleDateString() : '-'} &nbsp; 수정
                                </span>
                                <span class="p-2 ms-auto">
                                    <img src="/assets/Favorite.png" style="width: 30px; height: 30px">
                                    <span>${post.likeCount}</span>
                                    <img src="/assets/Comment.png" style="width: 30px; height: 30px">
                                    <span class="progress-bar-success" style="color: black">${post.commentCount}</span>
                                </span>
                            </p>
                        </div>
                    </div>
                    `;
                });
                $('#list').append(html);
                count++;
            } else {
                alert('더 이상 불러올 데이터가 없습니다.');
            }
        },
        error: function(error) {
            console.error('에러 발생:', error);
        }
    });
}

$('#loadMoreBtn').click(function() {
    loadMorePosts();
});