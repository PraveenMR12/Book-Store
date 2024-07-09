document.getElementById("deleteUser").addEventListener("click", function() {
    var proceed = confirm("Are you sure you want to proceed?");
    if (proceed) {
        window.location.href = "/user/home/deleteUser";
    }
});

function deleteUser(event, url) {
            event.preventDefault();
            const userConfirmed = confirm("Are you sure you want to proceed?");
            if (userConfirmed) {
                window.location.href = url;
            }
        }

function showalert() {
    alert("The button was clicked!");
}
function showAllUsersDiv() {
    window.location.href="/user/home/getAll";
}