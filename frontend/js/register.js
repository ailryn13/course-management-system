document.getElementById('registerForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    const studentData = { name: name, email: email, password: password };

    fetch('http://localhost:8080/api/students/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(studentData)
    })
    .then(response => {
        if (response.ok) return response.json();
        return response.json().then(errorData => { throw new Error(errorData.message) });
    })
    .then(data => {
        const msgElement = document.getElementById('message');
        msgElement.style.color = "green";
        msgElement.innerText = "Registration successful! Redirecting to login...";
        
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 2000);
    })
    .catch(error => {
        const msgElement = document.getElementById('message');
        msgElement.style.color = "red";
        msgElement.innerText = error.message;
    });
});