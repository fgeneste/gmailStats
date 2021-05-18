<template>
  <div>
    <div v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated">
      <h3>Récupération des messages</h3>
      <b-button @click="viderTable($event)" variant="danger">Vider tables</b-button>
      <b-button @click="recupererMessages($event)" variant="outline-primary">Recuperer messages</b-button>
      <b-button @click="stop($event)" variant="warning">STOP</b-button>

      <b-progress v-if="displayBar" :max="max" height="2rem">
        <b-progress-bar :value="value">
          <span
            >Progress: <strong>{{ value }} / {{ max }}</strong></span
          >
        </b-progress-bar>
      </b-progress>

      <h3>Statistiques</h3>
      <b-dropdown text="Sélection compte">
        <b-dropdown-item @click="onClick('fgeneste')">fgeneste</b-dropdown-item>
        <b-dropdown-item @click="onClick('franck.geneste')">franck.geneste</b-dropdown-item>
        <b-dropdown-item @click="onClick('frankouille')">frankouille</b-dropdown-item>
        <b-dropdown-item @click="onClick('fgth93@yahoo.fr')">fgth93</b-dropdown-item>
      </b-dropdown>

      <div v-if="displaySpinner" class="d-flex justify-content-center mb-3">
        <b-spinner label="Loading..."></b-spinner>
      </div>

      <table>
        <tr>
          <td>
            <zingchart v-if="displayPie" :data="myConfig" :height="300" :width="600" />
          </td>
          <td>
            <zingchart v-if="displayPie2" :data="myConfig4Void" :height="300" :width="600" />
          </td>
        </tr>
        <tr>
          <td>
            <ve-table v-if="displayPie" :columns="columns" :table-data="datastore"></ve-table>
          </td>
          <td></td>
        </tr>
      </table>
      <!--<zingchart v-if="displayPie" :data="myConfig" :height="300" :width="600"/>
          <ve-table v-if="displayPie" :columns="columns" :table-data="datastore"></ve-table>
          <zingchart v-if="displayPie2" :data="myConfig4Void" :height="300" :width="600"/>-->
    </div>
  </div>
</template>

<script lang="ts" src="./fgeneste.component.ts"></script>
