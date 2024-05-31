async function getList(pid, page = 1, size = 10) {
    const response = await axios.get(`/comments/list/${pid}`, {
        params: {page, size}
    });
    return response.data;
}

async function addComment(commentObj) {
    console.log("AddComment");
    const response = await axios.post('/comments/', commentObj);
    return response.data;
}

async function getComment(id) {
    const response = await axios.get(`/comments/${id}`);
    return response.data;
}

async function removeComment(id) {
    const response = await axios.delete(`/comments/${id}`);
    return response.data;
}