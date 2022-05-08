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
app.get("/:id/ticker/", (req, res) => {
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
    id: "BTC",
    high: 13459,
    low: 5353535,
    vol: 4242,
    last: 4242.4242,
    val: 24242,
    sell: 24424,
    date: "25/04/2424",
  },
];
