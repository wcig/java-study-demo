package main

import (
	"github.com/gin-gonic/gin"
)

type User struct {
	Id   int    `form:"id" json:"id"`
	Name string `form:"name" json:"name"`
}

type Result struct {
	Code int         `json:"code"`
	Data interface{} `json:"data"`
	Msg  string      `json:"msg"`
}

func ResultOk(c *gin.Context, v interface{}) {
	result := &Result{0, v, "ok"}
	c.JSON(200, result)
}

func ResultError(c *gin.Context, v interface{}, msg string) {
	result := &Result{-1, v, msg}
	c.JSON(200, result)
}

func main() {
	r := gin.Default()
	r.HEAD("/head", func(c *gin.Context) {
		user := &User{100, "tom"}
		ResultOk(c, user)
	})
	r.OPTIONS("/options", func(c *gin.Context) {
		c.Header("Access-Control-Allow-Origin", "*")
		c.Header("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, UPDATE")
		c.JSON(200, nil)
	})
	r.GET("/get", func(c *gin.Context) {
		var user User
		err := c.ShouldBindQuery(&user)
		if err != nil {
			ResultError(c, nil, err.Error())
			return
		}
		ResultOk(c, &user)
	})
	r.POST("/postJson", func(c *gin.Context) {
		var user User
		err := c.ShouldBindJSON(&user)
		if err != nil {
			ResultError(c, nil, err.Error())
			return
		}
		ResultOk(c, &user)
	})
	r.POST("/postFormUrlencoded", func(c *gin.Context) {
		var user User
		err := c.ShouldBind(&user)
		if err != nil {
			ResultError(c, nil, err.Error())
			return
		}
		ResultOk(c, &user)
	})
	r.POST("/postFormData", func(c *gin.Context) {
		var user User
		err := c.ShouldBind(&user)
		if err != nil {
			ResultError(c, nil, err.Error())
			return
		}
		fileHeader, err := c.FormFile("file")
		if err == nil {
			if err := c.SaveUploadedFile(fileHeader, "test.png"); err != nil {
				ResultError(c, nil, err.Error())
				return
			}
		}
		ResultOk(c, &user)
	})
	r.Run(":28080") // listen and serve on 0.0.0.0:8080 (for windows "localhost:8080")
}
