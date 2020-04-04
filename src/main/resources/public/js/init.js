let now = null;

$(document).ready(function () {
    run = async () => {
        await boardCreate();
        let response = await start();
        await addPieces(response);
    }
    run();
});

function move(now, dest) {
    let move_action = async () => {
        let response = await moveRequest(now, dest);
        if (response["error"]) {
            alert(response["error"]);
            return;
        }
        await deletePieces();
        await addPieces(response);
    }
    move_action();
}

function deletePieces() {
    return new Promise(((resolve, reject) => {
        $(".test").each((index, item) => {
            $(item).removeClass('p P q Q n N r R k K b B');
        })
        return resolve();
    }));
}


function boardCreate() {
    return new Promise(((resolve, reject) => {
        for (var i = 1; i <= 8; i++) {
            var row = $.parseHTML(`<div class="row"></div>`);
            for (var j = 0; j < 8; j++) {
                var colSymbol = String.fromCharCode(97 + j);
                var rowSymbol = 9 - i;

                if (j % 2 === i % 2) {
                    $(row).append(
                        `<div onclick="divClick(this)" class="col-1 border test" id="${colSymbol +
                        rowSymbol}"></div>`
                    );
                } else {
                    $(row).append(
                        `<div onclick="divClick(this)" class="col-1 border test black" id=${colSymbol +
                        rowSymbol}></div>`
                    );
                }
            }
            $("#table").append(row);
        }
        return resolve();
    }));
}

function start() {
    return new Promise(((resolve, reject) => {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                return resolve(JSON.parse(this.responseText));
            }
        };
        xhttp.open("GET", "http://localhost:8080/board", false);
        xhttp.setRequestHeader("X-Custom-Header", "testvalue");
        xhttp.send();
    }))
}


function addPieces(response) {
    return new Promise(((resolve, reject) => {
        let gameManager = response
        for (let location in gameManager["chessBoard"]["board"]) {
            let fileName = gameManager["chessBoard"]["board"][location]["name"];
            $('#' + location).addClass(fileName);
        }
        if (response["gameState"] === "RUNNING_WHITE_TURN") {
            $('#turn').removeClass("black-color");
            $('#turn').addClass("white-color");
        } else if (response["gameState"] === "RUNNING_BLACK_TURN") {
            $('#turn').removeClass("white-color");
            $('#turn').addClass("black-color");
        } else {
            $('#turn').remove();
            $.ajax({
                type: 'get',
                url: '/winner',
                dataType: 'json',
                error: function (xhr, status, error) {
                    return reject();
                },
                success: function (json) {
                    $('#guide').text('게임 종료! ' + json["winner"] + " 승리!");
                }
            })

        }
        return resolve();
    }))
}

function moveRequest(now, destination) {
    return new Promise(((resolve, reject) => {
        var queryString = `now=${now}&destination=${destination}`;
        $.ajax({
            type: 'post',
            url: '/move',
            data: queryString,
            dataType: 'json',
            error: function (xhr, status, error) {
                alert(error);
                return resolve();
            },
            success: function (json) {
                return resolve(json);
            }
        });
    }))
}


function isNotPieces(div) {
    let arr = ['b', 'B', 'k', 'K', 'n', 'N', 'p', 'P', 'q', 'Q', 'r', 'R'];
    let flag = true;
    arr.forEach(
        item => {
            let hasClass = $(div).hasClass(item);
            if (hasClass === true) {
                flag = false;
            }
        }
    )
    return flag;
}

function divClick(div) {
    if (now == null && isNotPieces(div)) {
        alert("출발지가 빈칸입니다.");
        return;
    }

    if (now == null) {
        now = div.id;
        $(div).addClass("border-danger");
    } else {
        $('#' + now).removeClass('border-danger');
        let nowCopy = now;
        now = null;
        move(nowCopy, div.id);
    }
}

