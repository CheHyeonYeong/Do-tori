/* getMyCommnets를 호출하면 데이터를 불러옴 */
const commentModule = {

    getMyComments: function (cri){

        let result = null;
        let query = '?';

        query += `pid=${dto.pid}&page=${cri.page}&size=10`;

        ajax({
            url: `/post/read${query}`,
            method: 'GET',
            async: false,
            loadend: (comments) => {

                result = JSON.parse(comments);
            }
        });

        return result;
    }
}

const CommentCriteria = {

    page: 1
}


const contentModule = {

    getMyContents: function (cri){

        let result = null;
        let query = '?';

        query += `page=${cri.page}`;

        $.ajax({
            url: `/post/list${query}`,
            method: 'GET',
            async: false,
            loadend: (contents) => {

                result = JSON.parse(contents);
            }
        });

        return result;
    }
}

const ContentCriteria = {

    page: 1
}
