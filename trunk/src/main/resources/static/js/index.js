$(function(){
	$("#query").click(function(){
		var word = $("#word").val();
		$.get("/musics",{word:word}, function(datas){
	    	if(datas!=null&&datas.length>0){
	    		var musicMap = {"musics":datas};
			  	var html = tmpl.render(musicMap);      // Render template using data - as HTML string
			    $("#result").html(html);      // Insert HTML string into DOM
	    	}else{
	    		$("#result").html("");
	    	}
		    window.location.hash = "word="+word;
	    })
	})
	var wordValue = $("#wordId").val();
	if(wordValue!=null&&wordValue!=""){
		$("#query").click();
	}
})
