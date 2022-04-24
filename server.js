const { time } = require("console");
const fs = require("fs");
const express = require("express");
const bodyParser = require("body-parser");
const app = express();

var date_time = new Date();

const port = 8000;
app.use(bodyParser.json());

app.listen(port, "0.0.0.0", () => {
  console.log("Servidor Mockup ON!");
});

//GET
app.get(
  "/api/users",
  (req, res, next) => {
    data("GET", req.ip);

    next();
  },
  (req, res) => {
    res.send(LISTA);
  }
);

//GET BY ID
app.get("/api/users/:id", (req, res) => {
  data("GET", req.ip);

  res.send(LISTA.find((list) => list.id == req.params.id));
});

//POST
app.post("/api/users", (req, res) => {
  let user = req.body;

  const firstId = LISTA
    ? Math.max.apply(
        null,
        LISTA.map((listIterator) => listIterator.id)
      ) + 1
    : 1;

  user.id = firstId;

  LISTA.push(user);

  data("POST", req.ip);

  res.status(201).send(user);
});

//PUT
app.put("/api/users/:id", (req, res) => {
  const user = {
    id: req.params.id,
    name: req.body.name,
    password: req.body.password,
  };

  const index = LISTA.findIndex((userIterator) => userIterator.id == user.id);

  LISTA[index] = user;

  data("PUT", req.ip);

  res.send(user);
});

//DELETE
app.delete("/api/users/:id", (req, res) => {
  LISTA = LISTA.filter((list) => list.id != req.params.id);

  data("DELETE", req.ip);

  res.status(204).send({});
});

//DATA
function data(args, ip) {
  fs.appendFile("log.txt", `${args} - ${ip} - ${date_time}\n`, function (err) {
    if (err) {
      throw err;
    }
  });
}

//BD MOCKUP
var LISTA = [
  {
    id: 1,
    name: {
      first_name: "Nair",
      about_name: "Sophie Daiane Almada",
    },
    email: "nair_almada@fitttransportes.com.br",
    phone_number: "(83) 99789-2255",
    cpf: "024.531.861-59",
    birthday: "05/01/1944",
    password: "YgKonlp1DQ",
  },
  {
    id: 2,
    name: {
      first_name: "Erick",
      about_name: "Manuel Lima",
    },
    email: "erick_manuel_lima@india.com",
    phone_number: "(27) 98613-1255",
    cpf: "051.610.742-99",
    birthday: "05/03/1976",
    password: "hBga3BdYKH",
  },
  {
    id: 3,
    name: {
      first_name: "Adriana",
      about_name: "Ester Isabela das Neves",
    },
    email: "adriana_dasneves@unifesp.br",
    phone_number: "(61) 99775-9642",
    cpf: "033.575.637-97",
    birthday: "09/03/1974",
    password: "x4CcaYhywF",
  },
  {
    id: 4,
    name: {
      first_name: "Bernardo",
      about_name: "Augusto Renan de Paula",
    },
    email: "bernardo-depaula71@machina8.com.br",
    phone_number: "(42) 99339-9386",
    cpf: "368.462.063-70",
    birthday: "07/02/1947",
    password: "Jr6MDIit8D",
  },
  {
    id: 6,
    name: {
      first_name: "Heitor",
      about_name: "Elias Marcos Araújo",
    },
    email: "heitor_araujo@platinium.com.br",
    phone_number: "(28) 98537-8580",
    cpf: "899.395.547-61",
    birthday: "11/02/1950",
    password: "orXmf1dbxy",
  },
  {
    id: 7,
    name: {
      first_name: "Elza",
      about_name: "Teresinha Josefa da Rocha",
    },
    email: "elza-darocha89@brf-br.com",
    phone_number: "(63) 98150-1398",
    cpf: "814.292.736-58",
    birthday: "25/01/1947",
    password: "AyMWFLMOne",
  },
];
