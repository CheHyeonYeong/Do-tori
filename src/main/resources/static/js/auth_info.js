function clickProfileImage() {
    document.getElementById('profileImageInput').click();
}

function showSubmitButton() {
    document.getElementsByClassName('profile_btn')[0].style.visibility = 'visible';
}

function previewImage(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            var profileImages = document.getElementsByClassName('profileImage');
            for (var i = 0; i < profileImages.length; i++) {
                profileImages[i].setAttribute('src', e.target.result);
            }
        }
        reader.readAsDataURL(input.files[0]);
    }
}


document.addEventListener('DOMContentLoaded', function() {
    const toggleBtn = document.querySelector('.toggle-btn');
    const aside = document.querySelector('.aside');
    const info = document.querySelector('.main_info_container')

    toggleBtn.addEventListener('click', function() {
        this.classList.toggle('active');
        aside.classList.toggle('active');
        info.classList.toggle('active');
    });
});