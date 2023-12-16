const drawTable = (characterData) => {
    const tableContainer = document.getElementById('table_div');

    if (!Array.isArray(characterData) || characterData.length === 0) {
        console.error('Invalid data format.');
        return;
    }

    const data = new google.visualization.DataTable();
    data.addColumn('string', 'Character Name');
    data.addColumn('string', 'Realm');
    data.addColumn('string', 'TOP 8 Dungeons');
    data.addColumn('number', 'Vault 1');
    data.addColumn('number', 'Vault 4');
    data.addColumn('number', 'Vault 8');
    data.addColumn('number', 'Total');

    if (characterData.length > 0) {
        characterData.forEach(({name, realm, dungeons, tierOneScore, tierFourScore, tierEightScore, totalScore}) => {
            let finishedDungeons = undefined;
            if (dungeons.length > 0) {
                finishedDungeons = dungeons.map(({shortName, finishedKeyLevel}) => {
                    return `${shortName} ${finishedKeyLevel}`;
                }).join(", ");
            }
            data.addRows([
                [name, realm, finishedDungeons, tierOneScore, tierFourScore, tierEightScore, totalScore]
            ]);
        });
    }
    const tableInstance = new google.visualization.Table(tableContainer);
    tableInstance.draw(data, {
        showRowNumber: true,
        width: '100%',
        height: '100%'
    });
}

const modal = () => {
    const closeModalBtn = document.getElementById('modalClose');
    const modal = document.getElementById('modal');

    modal.showModal();

    closeModalBtn.addEventListener('click', () => {
        modal.close();
    });

    window.addEventListener('click', ({target}) => {
        if (target === modal) {
            modal.close();
        }
    });
}

const showLoader = () => {
    const loaderContainer = document.getElementById('loaderContainer');
    loaderContainer.style.display = 'flex';
}

const hideLoader = () => {
    const loaderContainer = document.getElementById('loaderContainer');
    loaderContainer.style.display = 'none';
}

const setLocalStorageData = (localStorageName, jsonObject) => {
    const jsonString = JSON.stringify(jsonObject);
    localStorage.setItem(localStorageName, jsonString);
}

const getLocalStorageData = (localStorageName) => {
    const storedData = localStorage.getItem(localStorageName);
    return JSON.parse(storedData);
}

const setJsonObjectForLocalStorage = (name) => {
    const array = name.split(' ');

    const arrayOfNames = array.map((value) => ({"name": value}));
    setLocalStorageData('lastSearched', arrayOfNames)
}

const getJsonObjectForLocalStorage = (localStorageName) => {
    const lastSearchedData = getLocalStorageData(localStorageName);

    return lastSearchedData.map(({name}) => name).join(' ');
}

const fetchData = (name) => {
    const submitButton = document.getElementById('submit');
    const prevWeekCheckbox = document.getElementById('prevWeek');
    const dummyModeCheckbox = document.getElementById('dummyMode');
    const currentWeek = !prevWeekCheckbox.checked;
    const dummyMode = dummyModeCheckbox.checked;

    submitButton.disabled = true;
    showLoader();
    fetch(`/characters?name=${name}&currentWeek=${currentWeek}&dummyMode=${dummyMode}`)
        .then(response => response.json())
        .then((characterData) => {
            hideLoader();
            modal();
            drawTable(characterData);
            submitButton.disabled = false;
        })
        .catch(error => {
            hideLoader();
            submitButton.disabled = false;
            console.error('Error fetching character data:', error);
        });
}

const formSubmitHandler = () => {
    const form = document.getElementById('characterForm');
    const nameInput = document.getElementById('characterName');

    form.addEventListener('submit', (event) => {
        event.preventDefault();
        const name = nameInput.value;

        setJsonObjectForLocalStorage(name)
        fetchData(name)
    });
}

const lastSearchedHandle = () => {
    const lastSearchedButton = document.getElementById('lastSearched');

    lastSearchedButton.addEventListener('click', (event) => {
        event.preventDefault();
        const lastSearchedData = getJsonObjectForLocalStorage('lastSearched');

        fetchData(lastSearchedData);
    });
}

document.addEventListener('DOMContentLoaded', () => {
    lastSearchedHandle();
    formSubmitHandler();

    google.charts.load('current', {
        'packages': ['table']
    });
});
