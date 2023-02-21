package room

import "github.com/p2hacks2022/post-team05/internal/model"

// spacetimesにinsertする
func Post(words string, is_start bool) error {
	// dbmap初期化
	dbmap, err := model.InitDb()
	if err != nil {
		return err
	}
	defer dbmap.Db.Close()

	// dbmapにテーブル名登録
	dbmap.AddTableWithName(Room{}, "room")

	// Insert
	rooms := &Room{
		SecretWords: words,
		IsStart:     is_start,
	}
	err = dbmap.Insert(rooms)
	if err != nil {
		return err
	}

	return nil
}
