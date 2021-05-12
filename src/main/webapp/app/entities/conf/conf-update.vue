<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="gmailStatsApp.conf.home.createOrEditLabel"
          data-cy="ConfCreateUpdateHeading"
          v-text="$t('gmailStatsApp.conf.home.createOrEditLabel')"
        >
          Create or edit a Conf
        </h2>
        <div>
          <div class="form-group" v-if="conf.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="conf.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gmailStatsApp.conf.key')" for="conf-key">Key</label>
            <input
              type="text"
              class="form-control"
              name="key"
              id="conf-key"
              data-cy="key"
              :class="{ valid: !$v.conf.key.$invalid, invalid: $v.conf.key.$invalid }"
              v-model="$v.conf.key.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gmailStatsApp.conf.value')" for="conf-value">Value</label>
            <input
              type="text"
              class="form-control"
              name="value"
              id="conf-value"
              data-cy="value"
              :class="{ valid: !$v.conf.value.$invalid, invalid: $v.conf.value.$invalid }"
              v-model="$v.conf.value.$model"
            />
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.conf.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./conf-update.component.ts"></script>
