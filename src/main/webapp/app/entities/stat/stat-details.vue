<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="stat">
        <h2 class="jh-entity-heading" data-cy="statDetailsHeading">
          <span v-text="$t('gmailStatsApp.stat.detail.title')">Stat</span> {{ stat.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('gmailStatsApp.stat.from')">From</span>
          </dt>
          <dd>
            <span>{{ stat.from }}</span>
          </dd>
          <dt>
            <span v-text="$t('gmailStatsApp.stat.number')">Number</span>
          </dt>
          <dd>
            <span>{{ stat.number }}</span>
          </dd>
          <dt>
            <span v-text="$t('gmailStatsApp.stat.lastupdated')">Lastupdated</span>
          </dt>
          <dd>
            <span v-if="stat.lastupdated">{{ $d(Date.parse(stat.lastupdated), 'long') }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="stat.id" :to="{ name: 'StatEdit', params: { statId: stat.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./stat-details.component.ts"></script>
