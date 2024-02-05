function like(postId){
    var caller = event.target;
    const i  = caller.getElementsByTagName("i");
    var url = "/like";
    var n = 1;
    var classname = "fa-solid fa-heart";
    if(i[0].className==="fa-solid fa-heart"){
        url = "/unlike";
        n = -1;
        classname = "fa-regular fa-heart fa-bounce";
    }
    const formData = {
        postId: postId,
    };
    $.ajax({
        url: url,
        type: "POST",
        data: formData,
        success: function (response) {
            if (response === "Success") {
                caller.getElementsByClassName('label').item(0).textContent = Number(caller.getElementsByClassName('label').item(0).textContent) + n;
                i[0].className = classname;
            }
            else {
                let myToast = Toastify({
                    text: "Вы не зарегистрированы!",
                    duration: 5000
                }).showToast()
            }
        }
    });
}

function matchLike(){
    const i = document.getElementsByClassName("fa-regular fa-heart fa-bounce");
    i[i.length-1].className="fa-solid fa-heart";
}
