// 두 날짜 사이의 날짜들을 생성하는 헬퍼 함수
function getDates(startDate, stopDate) {
    let dateArray = [];
    let currentDate = new Date(startDate);
    while (currentDate <= stopDate) {
        dateArray.push(new Date(currentDate));
        currentDate.setDate(currentDate.getDate() + 1);
    }
    return dateArray;
}

// 오늘 날짜와 1년 전 날짜를 가져옴
const today = new Date();
const lastYear = new Date(today.getFullYear() - 1, today.getMonth(), today.getDate());

// 1년 전부터 오늘까지의 날짜 배열을 생성
const dates = getDates(lastYear, today);

// 각 날짜마다 무작위 기여 데이터를 생성
const data = dates.map(date => {
    return {
        date: date,
        count: Math.floor(Math.random() * 3)  // 0에서 4 사이의 무작위 기여 수
    };
});

// 그래프가 렌더링될 컨테이너 요소를 가져옴
const graphContainer = document.getElementById('contribution-graph');

// 표시할 월 레이블 정의
const monthLabels = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

// 월 레이블을 올바르게 표시하기 위해 현재 월을 추적
let currentMonth = lastYear.getMonth();

// 초기 월 레이블 추가
let monthDiv = document.createElement('div');
monthDiv.className = 'month-label';
monthDiv.innerText = monthLabels[currentMonth];
graphContainer.appendChild(monthDiv);

// 데이터를 순회하며 각 날짜를 렌더링
data.forEach((day, index) => {
    // 월이 변경되면 새로운 월 레이블을 추가
    if (day.date.getMonth() !== currentMonth) {
        currentMonth = day.date.getMonth();
        monthDiv = document.createElement('div');
        monthDiv.className = 'month-label';
        monthDiv.innerText = monthLabels[currentMonth];
        graphContainer.appendChild(monthDiv);
    }

    // 기여 수에 따라 적절한 클래스를 가진 div를 생성
    let dayDiv = document.createElement('div');
    dayDiv.className = 'day q' + day.count;
    dayDiv.title = `${day.date.toDateString()}: ${day.count} contributions`;

    // 해당 월의 컨테이너에 day div를 추가
    graphContainer.appendChild(dayDiv);

    // 매 7일마다 줄을 바꿔서 주간 행을 형성
    if ((index + 1) % 7 === 0) {
        let weekBreak = document.createElement('br');
        graphContainer.appendChild(weekBreak);
    }
});
