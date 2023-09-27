document.addEventListener('DOMContentLoaded', function() {
    const template = document.querySelector('template');
    const content = document.querySelector('content');
    content.innerHTML = template.innerHTML;
});