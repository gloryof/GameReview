thymol.configurePreExecution(function () {
    var data = {
        loginId: "test-user",
        errors: createErrorData(3)
    };

    thymol.requestContext.createVariable("it", data);
});