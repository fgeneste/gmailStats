<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="gmailStatsApp.message.home.createOrEditLabel"
          data-cy="MessageCreateUpdateHeading"
          v-text="$t('gmailStatsApp.message.home.createOrEditLabel')"
        >
          Create or edit a Message
        </h2>
        <div>
          <div class="form-group" v-if="message.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="message.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gmailStatsApp.message.account')" for="message-account">Account</label>
            <input
              type="text"
              class="form-control"
              name="account"
              id="message-account"
              data-cy="account"
              :class="{ valid: !$v.message.account.$invalid, invalid: $v.message.account.$invalid }"
              v-model="$v.message.account.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gmailStatsApp.message.from')" for="message-from">From</label>
            <input
              type="text"
              class="form-control"
              name="from"
              id="message-from"
              data-cy="from"
              :class="{ valid: !$v.message.from.$invalid, invalid: $v.message.from.$invalid }"
              v-model="$v.message.from.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gmailStatsApp.message.object')" for="message-object">Object</label>
            <input
              type="text"
              class="form-control"
              name="object"
              id="message-object"
              data-cy="object"
              :class="{ valid: !$v.message.object.$invalid, invalid: $v.message.object.$invalid }"
              v-model="$v.message.object.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gmailStatsApp.message.corps')" for="message-corps">Corps</label>
            <input
              type="text"
              class="form-control"
              name="corps"
              id="message-corps"
              data-cy="corps"
              :class="{ valid: !$v.message.corps.$invalid, invalid: $v.message.corps.$invalid }"
              v-model="$v.message.corps.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gmailStatsApp.message.date')" for="message-date">Date</label>
            <div class="d-flex">
              <input
                id="message-date"
                data-cy="date"
                type="datetime-local"
                class="form-control"
                name="date"
                :class="{ valid: !$v.message.date.$invalid, invalid: $v.message.date.$invalid }"
                :value="convertDateTimeFromServer($v.message.date.$model)"
                @change="updateInstantField('date', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gmailStatsApp.message.stillOnServer')" for="message-stillOnServer"
              >Still On Server</label
            >
            <input
              type="checkbox"
              class="form-check"
              name="stillOnServer"
              id="message-stillOnServer"
              data-cy="stillOnServer"
              :class="{ valid: !$v.message.stillOnServer.$invalid, invalid: $v.message.stillOnServer.$invalid }"
              v-model="$v.message.stillOnServer.$model"
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
            :disabled="$v.message.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./message-update.component.ts"></script>
