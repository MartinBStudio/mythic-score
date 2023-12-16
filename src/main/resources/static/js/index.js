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

const showLoader = (isLoading) => {
    const loaderContainer = document.getElementById('loaderContainer');

    if (isLoading) {
        loaderContainer.style.display = 'flex';
    } else {
        loaderContainer.style.display = 'none';
    }
}

const setDisableForAllButtons = (disableState) => {
    const submitButton = document.getElementById('submit');
    const allButton = document.getElementById('all');
    const r1Button = document.getElementById('r1');
    const r2Button = document.getElementById('r2');

    submitButton.disabled = disableState;
    allButton.disabled = disableState;
    r1Button.disabled = disableState;
    r2Button.disabled = disableState;
}

const fetchData = (apiName, apiSpecificVariable, apiSpecificValue) => {
    const prevWeekCheckbox = document.getElementById('prevWeek');
    const dummyModeCheckbox = document.getElementById('dummyMode');
    const currentWeek = !prevWeekCheckbox.checked;
    const dummyMode = dummyModeCheckbox.checked;

    setDisableForAllButtons(true);
    showLoader(true);
    fetch(`/${apiName}?${apiSpecificVariable}=${apiSpecificValue}&currentWeek=${currentWeek}&dummyMode=${dummyMode}`)
        .then(response => response.json())
        .then((characterData) => {
            showLoader(false);
            modal();
            drawTable(characterData);
            setDisableForAllButtons(false);
        })
        .catch(error => {
            showLoader(false);
            setDisableForAllButtons(false);
            console.error('Error fetching character data:', error);
        });
}

const formSubmitHandler = () => {
    const form = document.getElementById('characterForm');
    const nameInput = document.getElementById('characterName');

    form.addEventListener('submit', (event) => {
        event.preventDefault();
        const name = nameInput.value;

        fetchData('characters', 'name', name);
    });
}

const loadDataFromServerEnv = (nameEnv) => {
    const buttonId = document.getElementById(nameEnv.toLowerCase());

    buttonId.addEventListener('click', (event) => {
        event.preventDefault();

        fetchData('specificCharacters', 'type', nameEnv.toUpperCase());
    });
}

document.addEventListener('DOMContentLoaded', () => {
    loadDataFromServerEnv('ALL');
    loadDataFromServerEnv('R1');
    loadDataFromServerEnv('R2');
    formSubmitHandler();

    google.charts.load('current', {
        'packages': ['table']
    });
});
