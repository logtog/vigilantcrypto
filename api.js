const axios = require("axios");
const api = "http://localhost:8000/api/users/";

module.exports = api;

const log = (res) => console.log(res.data);

const getAll = () => axios.get(api).then(log).catch(console.error);

const getById = (id) => axios.get(`${api}${id}`).then(log).catch(console.error);

const create = (body) => axios.post(api, body).then(log).catch(console.error);

var test = {
  id: "",
  name: {
    first_name: "teste_name",
    about_name: "teste_about",
  },
  email: "test_mail",
  phone_number: "test_number",
  cpf: "test_cpf",
  birthday: "teste_birthday",
  password: "test_password",
};

create(test);
