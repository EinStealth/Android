package room

import "github.com/p2hacks2022/post-team05/internal/model"

func GetBySecretWords(words string) ([]Room, error) {
	// dbmap初期化
	dbmap, err := model.InitDb()
	if err != nil {
		return []Room{}, err
	}
	defer dbmap.Db.Close()

	// secret_words指定でroomのデータを取得
	var secret_words []Room
	_, err = dbmap.Select(&secret_words, "SELECT * FROM room WHERE secret_words=?", words)
	if err != nil {
		return []Room{}, err
	}

	return secret_words, nil
}
