$(function () {
    const DELAY = 6000;

    function setDate(date) {
        let d = date.getDate();
        let m = date.getMonth() + 1;
        let y = date.getFullYear();

        d = (d < 10) ? '0' + d : d;
        m = (m < 10) ? '0' + m : m;

        $('.date-time-date').html(`${d}.${m}.${y}`);
    }

    function setTime(time) {
        let h = time.getHours();
        let min = time.getMinutes();
        let sec = time.getSeconds();

        h = (h < 10) ? '0' + h : h;
        min = (min < 10) ? '0' + min : min;
        sec = (sec < 10) ? '0' + sec : sec;

        $('.date-time-time').html(`${h}:${min}:${sec}`);
    }

    function setCurrentDateTime() {
        let curTime = new Date();
        setDate(curTime);
        setTime(curTime);
    }

    setCurrentDateTime();
    setInterval(setCurrentDateTime, DELAY);
})