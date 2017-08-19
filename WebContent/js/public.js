$(document).ready(function(){

	$(".scoreInfo").click(function(){
		var word=$("[name=word]").val();
		var sentence=$("[name=sentence]").val();
		var translation=$("[name=translation]").val();
		if (word=="") {
				$(".sinfo").html("输入单词");
				$(".sinfo").css("display","block");
				return false;
		};
		if (sentence==""&&translation!=""||sentence!=""&&translation=="") {
				$(".sinfo").html("输入例句和翻译");
				$(".sinfo").css("display","block");
				return false;
		};
		$.ajax({
			type:"POST",
			url:"/AnkiWordGet/Addition",
			dataType:"html",
			data:{
				word:word,
				sentence:sentence,
				translation:translation,
			},
			beforeSend:function(){
					$(".sinfo").html("加载中...");
					$(".sinfo").css("display","block");
					$(".sinfo").removeClass("alert-error");
					$(".sinfo").removeClass("alert-success");
					$(".sinfo").addClass("alert-info");
			},
			success:function(data,textStatus){
					$(".sinfo").css("display","none");
					$(".sinfo").html(data);
					$(".sinfo").removeClass("alert-info");
					$(".sinfo").removeClass("alert-error");
					$(".sinfo").addClass("alert-success");
					$(".sinfo").css("display","block");
				
			},
			})
		
	})
	$('body').keypress(function(e){
	if(e.ctrlKey && e.which == 13 || e.which == 10) {
		$('#submit').click();
	}
	});
})