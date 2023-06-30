// Capture and store the scroll position
window.addEventListener('beforeunload', function () {
  localStorage.setItem('scrollPosition', window.pageYOffset);
});

// Restore the scroll position on page load
window.addEventListener('load', function () {
  var scrollPosition = localStorage.getItem('scrollPosition');
  window.scrollTo(0, scrollPosition);
});

