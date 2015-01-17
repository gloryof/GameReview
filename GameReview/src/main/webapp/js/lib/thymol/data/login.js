thymol.configurePreExecution(function () {
    var data = {
        loginId: "test-user",
        errors: createErrorData(3)
    };

    console.log(data);
    thymol.requestContext.createVariable("it", data);
});