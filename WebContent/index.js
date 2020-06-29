// Store class
class Store {
  constructor(initialState) {
    this.state = initialState;
  }

  getState() {
    return this.state;
  }

  addToState(contact) {
    this.state.push(contact);
  }

  setState(newState) {
    this.state = newState;
  }

  removeFromState(id) {
    const newState = this.state.filter((contact) => contact.id !== id);
    this.setState(newState);
  }
}

//Initialize State
const initialState = [];
const store = new Store(initialState);

// Contact Class: Represents a Contact
class Contact {
  constructor(name, email) {
    this.name = name;
    this.email = email;
  }
}

// UI Class: Handle UI Tasks
class UI {
  static displayContacts() {
    document.querySelector("#contact-list").innerHTML = "";
    fetch("http://localhost:4000/RestCrud/api/contacts")
      .then((res) => res.json())
      .then((contacts) => {
        store.setState(contacts);
        const stateContacts = store.getState();
        stateContacts.forEach((contact) => UI.addContactToList(contact));
      });
  }

  static addContactToList(contact) {
    const list = document.querySelector("#contact-list");
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${contact.id}</td>
      <td>${contact.name}</td>
      <td>${contact.email}</td>
      <td><div class="btn btn-outline-primary btn-sm edit">✏️</div></td>
      <td><div class="btn btn-outline-danger btn-sm delete">🗑️</div></td>
    `;
    row.addEventListener("click", () => UI.actionDispatch(contact));
    list.appendChild(row);
  }

  static actionDispatch(contact) {
    if (event.target.classList.contains("delete")) {
      const row = event.target.parentElement.parentElement;
      UI.deleteContact(contact.id, row);
    } else if (event.target.classList.contains("edit")) {
      UI.editContact(contact);
    }
  }

  static clearFields() {
    document.querySelector("#name").value = "";
    document.querySelector("#email").value = "";
  }

  static showAlert(message, className) {
    const div = document.createElement("div");
    div.className = `alert alert-${className}`;
    div.appendChild(document.createTextNode(message));
    const container = document.querySelector(".container");
    const form = document.querySelector("#contact-form");
    container.insertBefore(div, form);
    // Remove Alert in 3 seconds
    setTimeout(() => document.querySelector(".alert").remove(), 2000);
  }

  static deleteContact(id, row) {
    fetch(`http://localhost:4000/RestCrud/api/contacts/${id}`, {
      method: "DELETE",
    }).then(() => {
      //Remove element from state
      store.removeFromState(id);
      //Remove Element
      row.remove();
      //Show Success Message
      this.showAlert("Contact has been deleted.", "info");
    });
  }

  static editSubmitHandler() {
    const id = document.querySelector("#editId").value;
    const name = document.querySelector("#editName").value;
    const email = document.querySelector("#editEmail").value;
    console.log(id);
    console.log(name);
    console.log(email);
    if (name === "" || email === "") {
      UI.closeModel();
      return UI.showAlert("Name and email is required.", "danger");
    }
    if (name.length > 50) {
      UI.closeModel();
      return UI.showAlert("Name should be less than 50 characters.", "danger");
    }
    if (!isValidEmail(email)) {
      UI.closeModel();
      return UI.showAlert("Provide a valid email.", "danger");
    }
    ////Instantiate a Contact
    const editContactInstance = new Contact(name, email);
    fetch(`http://localhost:4000/RestCrud/api/contacts/${id}`, {
      method: "PUT",
      body: JSON.stringify(editContactInstance),
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then(() => {
        UI.displayContacts();
        UI.closeModel();
      })
      .catch((err) => {
        console.error(err);
        UI.closeModel();
      });
  }

  static closeModel() {
    const mainBody = document.querySelector("#mainBody");
    mainBody.classList.remove("blackoverlay");
    const modal = document.querySelector("#editModel");
    modal.style.display = "none ";
  }

  static editContact(contact) {
    const mainBody = document.querySelector("#mainBody");
    mainBody.classList.add("blackoverlay");
    const modal = document.querySelector("#editModel");
    modal.style.display = "block";
    const modalBody = document.querySelector("#editBody");
    modalBody.innerHTML = `
      <form>
        <input type="hidden" id="editId" value="${contact.id}" />
        <div class="form-group">
          <label for="editName">Name</label>
          <input type="text" id="editName" class="form-control"  value="${contact.name}" />
        </div>
        <div class="form-group">
          <label for="editEmail">Email</label>
          <input type="text" id="editEmail" class="form-control" value="${contact.email}" />
        </div>
      </form>
    `;
  }
}

const isValidEmail = (email) => {
  const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(String(email).toLowerCase()) && String(email).length < 50;
};

const contactSubmitHandler = (name, email) => {
  // Validate
  if (name === "" || email === "") {
    return UI.showAlert("Name and email is required.", "danger");
  }
  if (name.length > 50) {
    return UI.showAlert("Name should be less than 50 characters.", "danger");
  }
  if (!isValidEmail(email)) {
    return UI.showAlert("Provide a valid email.", "danger");
  }
  //Instantiate a Contact
  const contactInstance = new Contact(name, email);
  fetch("http://localhost:4000/RestCrud/api/contacts", {
    method: "post",
    body: JSON.stringify(contactInstance),
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((response) => response.json())
    .then((newContact) => {
      //Add Contact to Store
      store.addToState(newContact);
      //Clear fields
      UI.clearFields();
      //Add Contact to List
      UI.addContactToList(newContact);
      //Show Success Message
      UI.showAlert("Contact has been added.", "success");
    })
    .catch((err) => {
      console.error(err);
      //Show Success Message
      UI.showAlert("Internal Server Error.", "danger");
    });
};

// Event: Display Contact
document.addEventListener("DOMContentLoaded", UI.displayContacts);

// Event : Add a Contact
document.querySelector("#contact-form").addEventListener("submit", (e) => {
  //Get form values
  e.preventDefault();
  const name = document.querySelector("#name").value;
  const email = document.querySelector("#email").value;
  //return submit handler
  return contactSubmitHandler(name, email);
});
