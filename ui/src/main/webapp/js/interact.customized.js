interact('#target').draggable({
	inertia: true,
	//axis: 'x'
	snap: {
		targets: [interact.createSnapGrid({x:1,y:1})]
	},
	restrict: {
		restriction: {top: 200, left: 200, bottom: 500, right: 1000},
		endOnly: true
	},
	onmove: function (event) {
	    var target = event.target,
	        // keep the dragged position in the data-x/data-y attributes
	        x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx,
	        y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;
	    console.log(event.dx, event.dy);
	
	    // translate the element
	    target.style.webkitTransform =
	    target.style.transform =
	      'translate(' + x + 'px, ' + y + 'px)';
	
	    // update the posiion attributes
	    target.setAttribute('data-x', x);
	    target.setAttribute('data-y', y);
	  },
	  onend: function(event) {
		  console.log('event end')
	  }
});