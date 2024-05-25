document.addEventListener('DOMContentLoaded', function () {
    var calendarEl = document.getElementById('calendar');
    console.log(calendarEl)

    var calendar = new FullCalendar.Calendar(calendarEl, {

        headerToolbar: {
            left: 'prev',
            center: 'title',
            right: 'today next'
        },
        //캘린더 요일 변경
        dayHeaderContent: function (cell) {
            var weekday = cell.date.getDay();
            var koreanWeekdays = ['일', '월', '화', '수', '목', '금', '토'];
            return koreanWeekdays[weekday];
        },
        dayCellContent: function (cell) {
            var date = cell.date;
            var day = date.getDate();
            return day;
        },
        //캘린더 클릭하면 날짜 선택
        dateClick: function (info) {
            var selectedDate = info.dateStr;
            filterTodoList(selectedDate);

            // 이전에 선택된 날짜의 배경색 초기화
            var selectedElements = document.querySelectorAll('.fc-day.selected');
            selectedElements.forEach(function (element) {
                element.classList.remove('selected');
            });

            // 클릭한 날짜에 배경색 적용
            info.dayEl.classList.add('selected');

            // 선택한 날짜 표시
            var dateParts = selectedDate.split('-');
            var year = dateParts[0];
            var month = parseInt(dateParts[1]);
            var day = parseInt(dateParts[2]);

            var weekdays = ['일', '월', '화', '수', '목', '금', '토'];
            var weekday = weekdays[new Date(selectedDate).getDay()];

            var formattedDate = month + '월 ' + day + '일(' + weekday + ')';

            var selectedDateElement = document.getElementById('selectedDate');
            selectedDateElement.textContent = formattedDate;

            // Todo insertForm의 TodoDate 입력 필드 업데이트
            var todoDateInput = document.getElementById('todoDateInput');
            todoDateInput.value = selectedDate;

        }
    });

    calendar.render();

    //해당날짜로 리다이렉트 추가
    var urlParams = new URLSearchParams(window.location.search);
    var selectedDate = urlParams.get('selectedDate');

    if (selectedDate) {
        filterTodoList(selectedDate);
        calendar.gotoDate(selectedDate);
    } else {
        var today = new Date().toISOString().split('T')[0];
        filterTodoList(today);
    }

});


// 날짜에 해당하는 리스트만 표시
function filterTodoList(selectedDate) {
    var todoItems = document.querySelectorAll('.todo-item');
    todoItems.forEach(function (item) {
        var todoDate = item.querySelector('input[name="todoDate"]').value;
        if (todoDate === selectedDate) {
            item.style.display = 'block';
        } else {
            item.style.display = 'none';
        }
    });
}

// 각 todo 항목에 대한 클릭 이벤트 처리
document.querySelectorAll('.todo-item').forEach(function (todoDiv) {
    let isModifyMode = false;


    todoDiv.addEventListener('click', function () {
        event.stopPropagation(); // 이벤트 버블링 중지

        const buttons = todoDiv.querySelectorAll('button');

        if (buttons.length === 0) {
            const modifyButton = document.createElement('button');
            modifyButton.textContent = '수정';
            modifyButton.classList.add('modify-button');

            modifyButton.addEventListener('click', function () {
                if (isModifyMode) {
                    const form = document.createElement('form');
                    form.action = '/todo/modify';
                    form.method = 'post';

                    const inputs = todoDiv.querySelectorAll('input');

                    inputs.forEach(function (input) {
                        const inputClone = input.cloneNode(true);
                        form.appendChild(inputClone);
                    });

                    // 카테고리 필드 추가
                    const categorySelect = todoDiv.querySelector('select[name="category"]');
                    const categorySelectClone = categorySelect.cloneNode(true);
                    form.appendChild(categorySelectClone);

                    document.body.appendChild(form);
                    form.submit();
                    document.body.removeChild(form); //폼 제출후 제거
                } else {
                    const inputs = todoDiv.querySelectorAll('input');
                    inputs.forEach(function (input) {
                        input.removeAttribute('readonly');
                    });
                    isModifyMode = true;
                }
                const buttons = todoDiv.querySelectorAll('button');
                buttons.forEach(function (button) {
                    button.remove();
                });
            });

            const deleteButton = document.createElement('button');
            deleteButton.textContent = '삭제';
            deleteButton.classList.add('delete-button');

            deleteButton.addEventListener('click', function (event) {
                event.preventDefault();

                const form = document.createElement('form');
                form.action = '/todo/delete';
                form.method = 'post';
                const idInput = todoDiv.querySelector('input[name="id"]');
                const idInputClone = idInput.cloneNode(true);
                form.appendChild(idInputClone);

                // todoDate 파라미터 추가
                const todoDateInput = todoDiv.querySelector('input[name="todoDate"]');
                const todoDateInputClone = todoDateInput.cloneNode(true);
                form.appendChild(todoDateInputClone);

                document.body.appendChild(form);
                form.submit();
                document.body.removeChild(form); //폼 제출후 제거
            })

            // TodoDate 날짜 옆에 버튼 추가
            const todoDateInput = todoDiv.querySelector('input[name="todoDate"]');
            todoDateInput.insertAdjacentElement('afterend', deleteButton);
            todoDateInput.insertAdjacentElement('afterend', modifyButton);

        }
    })
});

// 다른 곳을 클릭하면 수정/삭제 버튼 제거
document.addEventListener('click', function (event) {
    const todoItems = document.querySelectorAll('.todo-item');
    todoItems.forEach(function (todoDiv) {
        const buttons = todoDiv.querySelectorAll('button');
        if (!todoDiv.contains(event.target) && buttons.length > 0) {
            buttons.forEach(function (button) {
                button.remove();
            });
        }
    });
});

// 체크박스 상태 설정
document.querySelectorAll('input[type="checkbox"]').forEach(function (checkbox) {
    checkbox.checked = checkbox.hasAttribute('checked');
    checkbox.setAttribute('readonly', 'readonly');
});