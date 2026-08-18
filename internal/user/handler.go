package user

import "net/http"

type Handler struct {
	business *Business
}

func NewHandler(business *Business) *Handler {
	return &Handler{business: business}
}

func (handler Handler) RegisterUser(res http.ResponseWriter, req *http.Request) {
	res.Write([]byte("Testing"))
}
