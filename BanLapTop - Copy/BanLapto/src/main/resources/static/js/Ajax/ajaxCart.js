var minus = document.querySelectorAll(".minus");
minus.forEach(function(item){
	item.addEventListener('click', function(item){
		
		var tr = item.target.closest("tr");
		var quantity = tr.querySelector("#quantity");
		quantity.value = quantity.value - 1;
		
		
	});
});


var add = document.querySelectorAll(".plus");
add.forEach(function(item){
	item.addEventListener('click',function(item){
		var tr = item.target.closest("tr");
		var quantity = tr.querySelector("#quantity");
		quantity.value = Number(quantity.value) + 1;
		
	})
})

