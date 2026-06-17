const baseUrl = "http://localhost:9090/account";
function showRegister() {
    document.getElementById("registerSection").style.display = "block";
}

function createAccount() {
    fetch(baseUrl + "/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            name: document.getElementById("name").value,
            email: document.getElementById("email").value,
            phone: document.getElementById("phone").value,
            passwordHash: document.getElementById("newPassword").value
        })
    })
    .then(res => res.json())
    .then(data => {
        alert("Account Created Successfully! Account Number: " + data.accountNumber);
    })
    .catch(error => {
        console.log(error);
        alert("Account creation failed");
    });
}


// Deposit Money
function depositMoney() {
    let acc = localStorage.getItem("loggedInAccount");
    let amount = document.getElementById("depositAmount").value;

    fetch(baseUrl + "/deposit?accountNumber=" + acc + "&amount=" + amount, {
        method: "POST"
    })
    .then(res => res.text())
    .then(data => {
        alert("Money Deposited Successfully");
    })
    .catch(error => {
        console.log(error);
        alert("Deposit Failed");
    });
}


// Withdraw Money
function withdrawMoney() {
    let acc = localStorage.getItem("loggedInAccount");
    let amount = document.getElementById("withdrawAmount").value;

    fetch(baseUrl + `/withdraw?accountNumber=${acc}&amount=${amount}`, {
        method: "POST"
    })
    .then(res => res.json())
    .then(data => {
        alert("Money Withdrawn Successfully");
    });
}

function checkBalance() {
    let acc = localStorage.getItem("loggedInAccount");

    console.log("Account Number:", acc);

    fetch("http://localhost:9090/account/balance/" + acc)
    .then(response => response.text())
    .then(data => {
        console.log(data);
        document.getElementById("balanceResult").innerHTML =
            "Current Balance: ₹" + data;
    })
    .catch(error => {
        console.log(error);
        alert("Balance fetch failed");
    });
}


// Transfer Money
function transferMoney() {
    let fromAcc = document.getElementById("fromAcc").value;
    let toAcc = document.getElementById("toAcc").value;
    let amount = document.getElementById("transferAmount").value;

    fetch(baseUrl + `/transfer?fromAccount=${fromAcc}&toAccount=${toAcc}&amount=${amount}`, {
        method: "POST"
    })
    .then(res => res.text())
    .then(data => {
        alert(data);
    });
}


function getTransactions() {
    let acc = localStorage.getItem("loggedInAccount");

    console.log("Transaction Account:", acc);

    fetch("http://localhost:9090/account/transactions/" + acc)
    .then(response => response.json())
    .then(data => {

        let result = "";

        if(data.length === 0){
            result = "No transactions found";
        }

        data.forEach(t => {
            result += `
                <div style="border:1px solid white; padding:10px; margin:10px;">
                    <p>From: ${t.fromAccount}</p>
                    <p>To: ${t.toAccount}</p>
                    <p>Amount: ₹${t.amount}</p>
                    <p>Type: ${t.type}</p>
                    <p>Status: ${t.status}</p>
                </div>
            `;
        });

        document.getElementById("historyResult").innerHTML = result;
    })
    .catch(error => {
        console.log(error);
        alert("Transaction history failed");
    });
}
function login() {
    let accNumber = document.getElementById("loginAcc").value;
    let password = document.getElementById("loginPass").value;

    fetch("http://localhost:9090/account/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            accountNumber: accNumber,
            passwordHash: password
        })
    })
    .then(response => response.json())
    .then(data => {
        console.log(data);

        if(data != null) {

            // save logged in account number
            localStorage.setItem(
                "loggedInAccount",
                data.accountNumber
            );

            alert("Login Successful");

            window.location.href = "/dashbord.html";
        }
        else {
            alert("Invalid account number or password");
        }
    })
    .catch(error => {
        console.log(error);
        alert("Login failed");
    });
}
    function downloadStatement() {
    let acc = document.getElementById("statementAcc").value;

    if(acc == ""){
        alert("Please enter account number");
        return;
    }

    window.open(
        "http://localhost:9090/account/statement/" + acc,
        "_blank"
    );
}


