var apiRootUrl = {};
$(function() {
    apiRootUrl = $("#api-root-url").val();
    console.log("JQuery initialized!");
    var demo = new Demo();
    $("#processOrder").click(function() {
        demo.processOrder();
    });
    demo.drawCars();
    demo.drawSuperHeroes();
});

function Demo() {
    var _this = this;

    this.processOrder = function() {        
        console.log("processOrder clicked!");
        $.post("https://com-dev-demo-api.azurewebsites.net/api/Order/process-order", function(data, status) {
            console.log("processing order...");
        });
    };

    this.drawSuperHeroes = function() {
        console.log("drawSuperHeroes clicked!");
        $.get(apiRootUrl + "/superHeroes.json", function(data, status){
            console.log(data);
            var source = $("#superHeroBladeTemplate").html();
            var compiledTemplate = Handlebars.compile(source);
            $("#superHeroBlade").html(compiledTemplate(data));
        });
    };

    this.drawCars = function() {
        console.log("drawCars clicked!");
        $.get(apiRootUrl + "/cars.json", function(data, status){
            console.log(data);
            var source = $("#carBladeTemplate").html();
            var compiledTemplate = Handlebars.compile(source);
            $("#carBlade").html(compiledTemplate(data));
        });
    };      
}

console.log("JavaScript initialized!");