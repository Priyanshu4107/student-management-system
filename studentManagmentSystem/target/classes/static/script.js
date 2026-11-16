
const API_URL = "http://localhost:8080/api/students";

const studentForm = document.getElementById("studentForm");
const studentTable = document.getElementById("studentTable");
const message = document.getElementById("message");

// Common API response handler
async function apiRequest(url, options = {}) {

    const response = await fetch(url, options);

    const text = await response.text();

    if (!response.ok) {
        throw new Error(text || `HTTP Error: ${response.status}`);
    }

    return text ? JSON.parse(text) : null;
}

// Display message
function showMessage(text) {
    message.textContent = text;

    setTimeout(() => {
        message.textContent = "";
    }, 3000);
}

// Get form data
function getFormData() {
    return {
        name: document.getElementById("name").value.trim(),
        age: Number(document.getElementById("age").value),
        email: document.getElementById("email").value.trim(),
        rollNo: Number(document.getElementById("rollNo").value),
        subject: document.getElementById("subject").value.trim()
    };
}

// CREATE STUDENT
studentForm.addEventListener("submit", async function(e) {

    e.preventDefault();

    const id = document.getElementById("studentId").value;
    const student = getFormData();

    try {

        if (id) {

            // UPDATE STUDENT
            await apiRequest(`${API_URL}/update/${id}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(student)
            });

            showMessage("Student updated successfully!");

        } else {

            // CREATE STUDENT
            await apiRequest(`${API_URL}/create`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(student)
            });

            showMessage("Student added successfully!");
        }

        resetForm();
        getAllStudents();

    } catch(error) {
        showMessage("Error: " + error.message);
        console.error(error);
    }

});

// GET ALL STUDENTS
async function getAllStudents() {

    try {

        const students = await apiRequest(`${API_URL}/getAll`);

        displayStudents(Array.isArray(students) ? students : []);

    } catch(error) {

        studentTable.innerHTML =
            `<tr><td colspan="7">No students found or API error.</td></tr>`;

        console.error(error);
        showMessage(error.message);
    }
}

// DISPLAY STUDENTS
function displayStudents(students) {

    studentTable.innerHTML = "";

    if (!students || students.length === 0) {

        studentTable.innerHTML =
            `<tr><td colspan="7">No student records available</td></tr>`;

        return;
    }

    students.forEach(student => {

        const row = document.createElement("tr");

        const values = [
            student.id,
            student.name,
            student.age,
            student.email,
            student.rollNo,
            student.subject
        ];

        values.forEach(value => {
            const td = document.createElement("td");
            td.textContent = value ?? "";
            row.appendChild(td);
        });

        const actionCell = document.createElement("td");

        const editButton = document.createElement("button");
        editButton.textContent = "Edit";
        editButton.className = "edit-btn";
        editButton.addEventListener("click", () => editStudent(student));

        const deleteButton = document.createElement("button");
        deleteButton.textContent = "Delete";
        deleteButton.className = "delete-btn";
        deleteButton.addEventListener("click", () => deleteStudent(student.id));

        const softButton = document.createElement("button");
        softButton.textContent = "Soft Delete";
        softButton.className = "soft-btn";
        softButton.addEventListener("click", () => softDeleteStudent(student.id));

        actionCell.append(editButton, deleteButton, softButton);
        row.appendChild(actionCell);

        studentTable.appendChild(row);
    });
}

// GET STUDENT BY ID
async function getStudentById() {

    const id = document.getElementById("searchId").value;

    if (!id) {
        showMessage("Please enter student ID");
        return;
    }

    try {

        const student = await apiRequest(`${API_URL}/get?id=${encodeURIComponent(id)}`);

        displayStudents(student ? [student] : []);

    } catch(error) {

        displayStudents([]);
        showMessage("Student not found: " + error.message);
    }
}

// EDIT STUDENT
function editStudent(student) {

    document.getElementById("studentId").value = student.id;
    document.getElementById("name").value = student.name;
    document.getElementById("age").value = student.age;
    document.getElementById("email").value = student.email;
    document.getElementById("rollNo").value = student.rollNo;
    document.getElementById("subject").value = student.subject;

    document.getElementById("formTitle").textContent = "Update Student";
    document.getElementById("submitBtn").textContent = "Update Student";

    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });
}

// DELETE STUDENT PERMANENTLY
async function deleteStudent(id) {

    if (!confirm("Are you sure you want to permanently delete this student?")) {
        return;
    }

    try {

        await apiRequest(`${API_URL}/delete/${id}`, {
            method: "DELETE"
        });

        showMessage("Student permanently deleted!");
        getAllStudents();

    } catch(error) {
        showMessage("Delete failed: " + error.message);
    }
}

// SOFT DELETE STUDENT
async function softDeleteStudent(id) {

    if (!confirm("Move this student to deleted records?")) {
        return;
    }

    try {

        await apiRequest(`${API_URL}/delete-soft/${id}`, {
            method: "PATCH"
        });

        showMessage("Student soft deleted!");
        getAllStudents();

    } catch(error) {
        showMessage("Soft delete failed: " + error.message);
    }
}

// RESET FORM
function resetForm() {

    studentForm.reset();

    document.getElementById("studentId").value = "";

    document.getElementById("formTitle").textContent =
        "Add New Student";

    document.getElementById("submitBtn").textContent =
        "Add Student";
}

// LOAD STUDENTS WHEN PAGE OPENS
getAllStudents();
