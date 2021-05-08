<template>
    <div>
        <div v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated">
            <h3>Récupération des messages</h3>
            <b-button @click="viderTable($event)" variant="danger">Vider tables</b-button>
            <b-button @click="recupererMessages($event)" variant="outline-primary">Recuperer messages</b-button>
        </div>
    </div>
</template>

<script lang="ts" src="./fgeneste.component.ts">
</script>
