// Fonction pour appliquer un thème
function applyTheme(theme) {
    const body = document.body;

    // Supprimer toutes les classes de thème existantes
    body.classList.remove('light-theme', 'dark-theme', 'blue-theme', 'red-theme');

    // Ajouter la classe du thème sélectionné
    body.classList.add(`${theme}-theme`);

    // Enregistrer le choix de l'utilisateur dans localStorage
    localStorage.setItem('theme', theme);
}

// Appliquer le thème sauvegardé au chargement de la page
function applySavedTheme() {
    const savedTheme = localStorage.getItem('theme') || 'light'; // Par défaut : thème clair
    applyTheme(savedTheme);

    // Mettre à jour le menu déroulant ou les boutons
    const themeSelect = document.getElementById('themeSelect');
    if (themeSelect) {
        themeSelect.value = savedTheme;
    }

    const themeButtons = document.querySelectorAll('.theme-selector button');
    if (themeButtons) {
        themeButtons.forEach(button => {
            if (button.getAttribute('data-theme') === savedTheme) {
                button.classList.add('active');
            } else {
                button.classList.remove('active');
            }
        });
    }
}

// Synchroniser le thème entre les pages
function syncTheme() {
    window.addEventListener('storage', (event) => {
        if (event.key === 'theme') {
            applyTheme(event.newValue);
        }
    });
}

// Écouter les changements dans le menu déroulant
const themeSelect = document.getElementById('themeSelect');
if (themeSelect) {
    themeSelect.addEventListener('change', (event) => {
        applyTheme(event.target.value);
    });
}

// Écouter les clics sur les boutons de thème
const themeButtons = document.querySelectorAll('.theme-selector button');
if (themeButtons) {
    themeButtons.forEach(button => {
        button.addEventListener('click', () => {
            const theme = button.getAttribute('data-theme');
            applyTheme(theme);
        });
    });
}

// Appliquer le thème sauvegardé au chargement de la page
window.addEventListener('load', applySavedTheme);

// Synchroniser le thème entre les pages
syncTheme();
// Afficher/masquer le menu déroulant des thèmes
document.getElementById('themeSelector').addEventListener('click', function (event) {
    event.preventDefault(); // Empêcher le comportement par défaut du lien
    const dropdown = document.getElementById('themeDropdown');
    dropdown.style.display = dropdown.style.display === 'block' ? 'none' : 'block';
});

// Appliquer le thème sélectionné
document.querySelectorAll('.theme-dropdown a').forEach(link => {
    link.addEventListener('click', function (event) {
        event.preventDefault(); // Empêcher le comportement par défaut du lien
        const theme = this.getAttribute('data-theme');
        applyTheme(theme); // Appliquer le thème
        document.getElementById('themeDropdown').style.display = 'none'; // Masquer le menu déroulant
    });
});

// Fonction pour appliquer un thème
function applyTheme(theme) {
    const body = document.body;

    // Supprimer toutes les classes de thème existantes
    body.classList.remove('light-theme', 'dark-theme', 'blue-theme', 'red-theme');

    // Ajouter la classe du thème sélectionné
    body.classList.add(`${theme}-theme`);

    // Enregistrer le choix de l'utilisateur dans localStorage
    localStorage.setItem('theme', theme);
}

// Appliquer le thème sauvegardé au chargement de la page
function applySavedTheme() {
    const savedTheme = localStorage.getItem('theme') || 'light'; // Par défaut : thème clair
    applyTheme(savedTheme);
}

// Appliquer le thème au chargement de la page
window.addEventListener('load', applySavedTheme);