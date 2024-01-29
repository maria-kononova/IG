function showMessage(date) {
    //2024-01-25T18:39:10.000+03:00
    //Thu Jan 25 2024 03:00:00 GMT+0300 (Москва, стандартное время)
    //var options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    var optionsDay = {day: 'numeric', month: 'short'};
    let p = document.getElementsByClassName("date_p_post_item_list");
    let newDate = new Date(date.split('T')[0]);
    let newTime = date.split('T')[1].split(':')[0] + ":" + date.split('T')[1].split(':')[1];
    let dateString = newDate.toLocaleDateString("ru-Ru", optionsDay);
    let dif = checkDate(date);
    let result = "";
    if(dif > 31536000000){
        result = dateString.substring(0, dateString.length - 1) + " " +date.split('-')[0];
    }
    else if (dif < 21600000) {
        result = difString(dif);
    }
    else {
        result = dateString.substring(0, dateString.length - 1) + " в " + newTime;
    }
    p[p.length - 1].textContent += result;
}

function checkDate(date) {
    //var options = { year: 'numeric',  month: 'numeric', day: 'numeric', hour: 'numeric', minute: 'numeric', second: 'numeric'};
    let checkDate = new Date(date);
    let curDate = new Date();
    return curDate - checkDate;
}

function difString(dif) {
    if (dif < 60000) return "меньше минуты назад";
    if (dif < 1800000) {
        let minutes = Math.floor((dif / (1000 * 60)) % 60);
        return minutes + " " + check(minutes, "минуту", "минуты", "минут") + " назад";
    }
    if (dif >= 1800000 && dif <= 3600000) return "меньше часа назад";
    if(dif > 3600000) {
        let hours = Math.floor((dif / (1000 * 60 * 60)) % 24);
        return hours + " " + check(hours, "час", "часа", "часов") + " назад";
    }
    else {
        return "когда-то"
    }
}

function check(number, one, two, five) {
    let n = Math.abs(number);
    n %= 100;
    if (n >= 5 && n <= 20) {
        return five;
    }
    n %= 10;
    if (n === 1) {
        return one;
    }
    if (n >= 2 && n <= 4) {
        return two;
    }
    return five;
}