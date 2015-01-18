/**
 * ベースデータを作成する.
 * @returns {createBaseData.data} ベースデータ
 */
var createBaseData = function () {
    var data = {};

    data.genreSearch = {
        genres: [
            createGenreData(1, "アクション"),
            createGenreData(2, "RPG"),
            createGenreData(3, "シミュレーション")
        ]
    };

    return data;
};

/**
 * ジャンルデータの作成.
 * @param {Number} id ジャンルID
 * @param {String} title ジャンルタイトル
 * @returns {createGenreData.base-dataAnonym$3} ジャンルデータオブジェクト
 */
var createGenreData = function (id, title) {

    return {
        id: id,
        title: title
    };
};

/**
 * エラーメッセージデータの作成
 * @param {type} count 件数
 * @returns {createErrorData.base-dataAnonym$2} エラー情報オブジェクト
 */
var createErrorData = function (count) {

    var errorList = [];

    for (var i = 0; i < count; i++) {

        errorList.push({
            getMessage: function () {
                return "エラーメッセージ";
            }
        });
    }

    return {
        hasError: function () {
            return true;
        },
        toList: function () {
            return errorList;
        }
    };
};