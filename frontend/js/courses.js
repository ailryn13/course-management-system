const studentId = localStorage.getItem('loggedInStudentId');

if (!studentId) {
    alert("Please login first!");
    window.location.href = 'login.html';
}

window.onload = function() {
    fetchCourses();
};

function fetchCourses() {
    fetch('http://localhost:8080/api/courses')
        .then(response => response.json())
        .then(courses => {
            const tbody = document.getElementById('courseTableBody');
            tbody.innerHTML = ''; 

            courses.forEach(course => {
                const isFull = course.availableSeats <= 0;
                const buttonHtml = `<button class="success-btn" onclick="enrollCourse(${course.id})" ${isFull ? 'disabled' : ''}>Enroll</button>`;

                const row = `<tr>
                    <td>${course.id}</td>
                    <td>${course.name}</td>
                    <td>${course.description}</td>
                    <td>${course.duration}</td>
                    <td>${course.availableSeats}</td>
                    <td>${buttonHtml}</td>
                </tr>`;
                tbody.innerHTML += row;
            });
        })
        .catch(error => console.error('Error fetching courses:', error));
}

function showAlert(title, message) {
    document.getElementById('alertTitle').innerText = title;
    document.getElementById('alertMessage').innerText = message;
    document.getElementById('alertModal').style.display = 'flex';
}

function closeAlertModal() {
    document.getElementById('alertModal').style.display = 'none';
}

function enrollCourse(courseId) {
    const msgElement = document.getElementById('message');
    msgElement.style.color = "blue";
    msgElement.innerText = "Enrolling...";

    fetch(`http://localhost:8080/api/enroll/${courseId}?studentId=${studentId}`, { method: 'POST' })
    .then(response => {
        if (response.ok) {
            showAlert("Success!", "Successfully enrolled in the course!");
            fetchCourses(); 
            msgElement.innerText = "";
        } else {
            return response.text().then(text => { throw new Error(text) });
        }
    })
    .catch(error => {
        showAlert("Enrollment Failed", error.message);
        msgElement.innerText = "";
    });
}