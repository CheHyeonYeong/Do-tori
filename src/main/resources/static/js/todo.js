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
            selectedDateElement.innerHTML = '';
            selectedDateElement.textContent = formattedDate;

            // Todo insertForm의 TodoDate 입력 필드 업데이트
            var todoDateInput = document.getElementById('todoDateInput');
            todoDateInput.value = selectedDate;

        }
    });

    calendar.render();

    // 그래프가 렌더링될 컨테이너 요소를 가져옴
    const contribution = document.getElementById('contribution');

    calendar.on('datesSet', function (info) {
        var startDate = new Date(info.start);
        startDate.setDate(startDate.getDate() + 15);
        var currentMonth = startDate.getMonth();
        var currentYear = startDate.getUTCFullYear();
        var daysInMonth = new Date(startDate.getFullYear(), startDate.getMonth() + 1, 0).getDate();

        // 그래프 렌더링 함수 호출
        renderGraph(currentMonth, currentYear, daysInMonth);
    });

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


// 주어진 년도와 월에 해당하는 일 수를 반환하는 함수입니다.
function generateRandomData(startDate, daysInMonth) {
    var data = [];
    for (var i = 1; i <= daysInMonth; i++) {
        var date = new Date(startDate.getFullYear(), startDate.getMonth(), i);
        var count =  // 해당 done 개수
        data.push({ date: date, count: count });
    }
    return data;
}
// 카테고리 별로 날짜마다 done의 개수를 세는 함수
function countDoneByCategoryAndDate() {
    var categoryDoneCountByDate = {
        'No category': {},
        '일정': {},
        '공부': {},
        '습관': {}
    };

    $('.todo-item').each(function() {
        var category = $(this).find('select[name="category"]').val();
        var todoDate = $(this).find('input[name="todoDate"]').val();
        var isDone = $(this).find('input[name="done"]').prop('checked');

        var formattedDate = todoDate.split('-').join('');

        if (category in categoryDoneCountByDate && formattedDate) {
            if (!(formattedDate in categoryDoneCountByDate[category])) {
                categoryDoneCountByDate[category][formattedDate] = 0;
            }
            if (isDone) {
                categoryDoneCountByDate[category][formattedDate]++;
            }
        }
    });

    return categoryDoneCountByDate;
}


function findXPositionOfClass(specialClass) {
    let elements = document.querySelectorAll(`.${specialClass}`);
    let xPositions;
    elements.forEach(element => {
        let rect = element.getBoundingClientRect();
        console.log(`${specialClass} element X position: ${rect.x}`);
        xPositions = rect.x;
    });
    return xPositions;
}


// 그래프 렌더링 함수
function renderGraph(currentMonth, currentYear, daysInMonth) {
    // 그래프가 렌더링될 컨테이너 요소를 가져옴
    const contribution = document.getElementById('contribution');

    // 월 레이블 변경
    let monthDiv = contribution.querySelector('.month-label');
    monthDiv.innerHTML = `<h3>${currentYear}.${currentMonth < 9 ? '0' + (currentMonth + 1) : currentMonth + 1}</h3>`;

    let graphContainer = document.getElementById('habbit-tracker');

    // 초기화
    graphContainer.innerHTML = '';

    // 요소 생성
    let dateRangeDiv = document.createElement('div');
    dateRangeDiv.className = 'date-range';

    // 카테고리별로 컨테이너 생성
    let categories = ['No category', '일정', '공부', '습관'];
    let categoryContainers = {};

    categories.forEach(category => {
        let categoryDiv = document.createElement('div');
        categoryDiv.className = 'category-container';
        categoryDiv.id = category.replace(' ', '-').toLowerCase(); // id를 소문자와 하이픈으로 설정
        categoryDiv.innerHTML = `${category === 'No category' ? '없음&nbsp;' : category + '&nbsp;'}`;

        // flex를 이용하여 왼쪽 정렬 및 간격 조정
        categoryDiv.style.display = 'flex';
        categoryDiv.style.justifyContent = 'flex-start';
        categoryDiv.style.gap = '4px';

        graphContainer.appendChild(categoryDiv);
        categoryContainers[category] = categoryDiv;
    });

    var result = countDoneByCategoryAndDate();

    // 현재 달의 모든 날짜에 대해 Div 생성
    for (let day = 1; day <= daysInMonth; day++) {
        let currentDate = new Date(currentYear, currentMonth, day);
        let year = currentDate.getFullYear();
        let month = String(currentDate.getMonth() + 1).padStart(2, '0');
        let dayString = String(currentDate.getDate()).padStart(2, '0');
        let formattedDate = `${year}${month}${dayString}`;

        categories.forEach(category => {
            let doneCount = result[category][formattedDate] || 0;
            let className = '';

            if (doneCount === 0) {
                className = 'day q0';
            } else if (doneCount >= 1 && doneCount <= 4) {
                className = 'day q1';
            } else {
                className = 'day q2';
            }

            if(day == 10){
                className += ' tenth'
            } else if(day == 20){
                className += ' twentieth'
            } else if(day == 30){
                className += ' thirtieth'
            }


            let dayDiv = document.createElement('div');
            dayDiv.className = className;
            dayDiv.title = `${formattedDate}: ${doneCount} done tasks`;
            categoryContainers[category].appendChild(dayDiv);
        });
    }

    // 모든 요소가 생성된 후에 X 위치 가져오기
    let tenthXPosition = findXPositionOfClass('tenth');
    let twentiethXPosition = findXPositionOfClass('twentieth');
    let thirtiethXPosition = findXPositionOfClass('thirtieth');

    // 가져온 X 위치를 이용하여 span 요소의 위치 조정
    let dateSpans = document.querySelectorAll('.date-range .date');
    dateSpans.forEach((dateSpan, index) => {
        if (index === 0) {
            dateSpan.style.left = `${tenthXPosition}px`;
        } else if (index === 1) {
            dateSpan.style.left = `${twentiethXPosition}px`;
        } else if (index === 2) {
            dateSpan.style.left = `${thirtiethXPosition}px`;
        }
    });
}