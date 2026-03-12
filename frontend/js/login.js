document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const credentials = { email: email, password: password };

    fetch('http://localhost:8080/api/students/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(credentials)
    })
    .then(response => {
        if (response.ok) return response.json();
        return response.text().then(text => { throw new Error(text) });
    })
    .then(data => {
        localStorage.setItem('loggedInStudentId', data.studentId);
        localStorage.setItem('loggedInStudentName', data.name);
        
        const msgElement = document.getElementById('message');
        msgElement.style.color = "green";
        msgElement.innerText = "Login successful! Redirecting...";
        
        setTimeout(() => {
            window.location.href = 'dashboard.html';
        }, 1000);
    })
    .catch(error => {
        const msgElement = document.getElementById('message');
        msgElement.style.color = "red";
        msgElement.innerText = error.message;
    });
});