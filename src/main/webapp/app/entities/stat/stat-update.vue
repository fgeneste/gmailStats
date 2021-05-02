<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="gmailStatsApp.stat.home.createOrEditLabel"
          data-cy="StatCreateUpdateHeading"
          v-text="$t('gmailStatsApp.stat.home.createOrEditLabel')"
        >
          Create or edit a Stat
        </h2>
        <div>
          <div class="form-group" v-if="stat.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="stat.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gmailStatsApp.stat.from')" for="stat-from">From</label>
            <input
              type="text"
              class="form-control"
              name="from"
              id="stat-from"
              data-cy="from"
              :class="{ valid: !$v.stat.from.$invalid, invalid: $v.stat.from.$invalid }"
              v-model="$v.stat.from.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gmailStatsApp.stat.number')" for="stat-number">Number</label>
            <input
              type="number"
              class="form-control"
              name="number"
              id="stat-number"
              data-cy="number"
              :class="{ valid: !$v.stat.number.$invalid, invalid: $v.stat.number.$invalid }"
              v-model.number="$v.stat.number.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gmailStatsApp.stat.lastupdated')" for="stat-lastupdated">Lastupdated</label>
            <div class="d-flex">
              <input
                id="stat-lastupdated"
                data-cy="lastupdated"
                type="datetime-local"
                class="form-control"
                name="lastupdated"
                :class="{ valid: !$v.stat.lastupdated.$invalid, invalid: $v.stat.lastupdated.$invalid }"
                :value="convertDateTimeFromServer($v.stat.lastupdated.$model)"
                @change="updateInstantField('lastupdated', $event)"
              />
            </div>
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
            :disabled="$v.stat.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./stat-update.component.ts"></script>
