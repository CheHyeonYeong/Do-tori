let currentPage = 1;
const size = 10;

function loadMoreData() {
    $.ajax({
        url: '/post/list',
        data: {
            page: currentPage,
            size: size
        },
        success: function(responseDTO) {
            const postLists = responseDTO.postLists;
            if (postLists.length > 0) {
                // 게시글 목록을 DOM에 추가하는 로직
                let html = '';
                postLists.forEach(function(post) {
                    html += `
            <div class="card mx-auto p-2 vstack gap-3" style="width: 50%; margin: 50px">
              <!-- 카드 내용 -->
              <div class="card-header">
                <img th:src="@{/assets/Profile.png}" class="profile" id="profileImage" style="width: 30px; height: auto">
                <div class="card-title" style="display:inline; font-weight: bold">${post.nickName}</div>
                <!-- 수정/삭제 버튼 -->
              </div>
              <div class="card-body">
                <div class="input-group mb-3">
                  <input type="hidden" class="form-control" value="${post.pid}" readonly>
                </div>
                <div class="input-group mb-3" style="display: block; text-align: center; cursor: pointer">
                  <div class="body-content" style="text-align: left">${post.content}</div>
                  ${post.thumbnail ? `<img src="/images/${post.thumbnail}" width="40">` : ''}
                </div>
              </div>
              <div style="margin: 0 auto; width: 95%">
                <hr>
                <p class="hstack gap-3 date">
                  <span>${new Date(post.regDate).toLocaleDateString()}</span>
                  <span>/</span>
                  <span>
                    ${post.modDate ? new Date(post.modDate).toLocaleDateString() : '-'} &nbsp; 수정
                  </span>
                  <span class="p-2 ms-auto">
                    <img th:src="@{/assets/Favorite.png}" style="width: 30px; height: 30px">
                    <span>${post.likeCount}</span>
                    <img th:src="@{/assets/Comment.png}" style="width: 30px; height: 30px">
                    <span class="progress-bar-success" style="color: black">${post.commentCount}</span>
                  </span>
                </p>
              </div>
            </div>
          `;
                });
                $('#list').append(html);
                currentPage++;
            } else {
                // 더 이상 불러올 데이터가 없는 경우 처리 로직
                alert('더 이상 불러올 데이터가 없습니다.');
            }
        },
        error: function(error) {
            console.error('에러 발생:', error);
        }
    });
}

function handleScroll() {
    const scrollHeight = document.documentElement.scrollHeight;
    const scrollTop = document.documentElement.scrollTop;
    const clientHeight = document.documentElement.clientHeight;

    if (scrollTop + clientHeight >= scrollHeight) {
        loadMoreData();
    }
}

window.addEventListener('scroll', handleScroll);