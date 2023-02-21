package main

import "github.com/EinStealth/Android/internal/controller"

// @title          Swagger Example API
// @version        1.0
// @description    This is a sample server celler server.
// @termsOfService http://swagger.io/terms/
func main() {
	router := controller.GetRouter()
	router.Run(":8080")
}
