package room

type Room struct {
	Id     int `db:"id"`
	SecretWords string `db:"secret_words"`
	IsStart bool `db:"is_start"`
}
