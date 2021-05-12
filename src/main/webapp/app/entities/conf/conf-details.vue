<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="conf">
        <h2 class="jh-entity-heading" data-cy="confDetailsHeading">
          <span v-text="$t('gmailStatsApp.conf.detail.title')">Conf</span> {{ conf.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('gmailStatsApp.conf.key')">Key</span>
          </dt>
          <dd>
            <span>{{ conf.key }}</span>
          </dd>
          <dt>
            <span v-text="$t('gmailStatsApp.conf.value')">Value</span>
          </dt>
          <dd>
            <span>{{ conf.value }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="conf.id" :to="{ name: 'ConfEdit', params: { confId: conf.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./conf-details.component.ts"></script>
