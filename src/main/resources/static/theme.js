// Fonction pour basculer entre les thèmes
function toggleTheme() {
    const body = document.body;
    const themeToggle = document.getElementById('themeToggle');
    const isDarkTheme = body.classList.toggle('dark-theme');

    // Mettre à jour l'icône et le texte du bouton
    if (isDarkTheme) {
        themeToggle.innerHTML = '<i class="fas fa-sun"></i> Thème clair';
    } else {
        themeToggle.innerHTML = '<i class="fas fa-moon"></i> Thème sombre';
    }

    // Enregistrer le choix de l'utilisateur dans localStorage
    localStorage.setItem('theme', isDarkTheme ? 'dark' : 'light');
}

// Appliquer le thème au chargement de la page
function applySavedTheme() {
    const savedTheme = localStorage.getItem('theme');
    const body = document.body;
    const themeToggle = document.getElementById('themeToggle');

    if (savedTheme === 'dark') {
        body.classList.add('dark-theme');
        themeToggle.innerHTML = '<i class="fas fa-sun"></i> Thème clair';
    } else {
        body.classList.remove('dark-theme');
        themeToggle.innerHTML = '<i class="fas fa-moon"></i> Thème sombre';
    }
}

// Synchroniser le thème entre les pages
function syncTheme() {
    window.addEventListener('storage', (event) => {
        if (event.key === 'theme') {
            applySavedTheme();
        }
    });
}

// Écouter le clic sur le bouton de bascule de thème
document.getElementById('themeToggle').addEventListener('click', toggleTheme);

// Appliquer le thème sauvegardé au chargement de la page
window.addEventListener('load', applySavedTheme);

// Synchroniser le thème entre les pages
syncTheme();