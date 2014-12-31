thymol.configurePreExecution(function () {
    var data = createBaseData();
    var review = {
        reviews: [
            {
                title: "Thymol.jsのタイトル",
                goodPoint: "Thymol.jsにより埋め込んだコメントです",
                badPoint: "Thymol.jsにより埋め込んだコメントです2",
                comment: "Thymol.jsにより埋め込んだコメントです3"
            }
        ]
    };

    data.review = review;
    thymol.requestContext.createVariable("it", data);
});