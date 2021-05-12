<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="gmailStatsApp.attachement.home.createOrEditLabel"
          data-cy="AttachementCreateUpdateHeading"
          v-text="$t('gmailStatsApp.attachement.home.createOrEditLabel')"
        >
          Create or edit a Attachement
        </h2>
        <div>
          <div class="form-group" v-if="attachement.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="attachement.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gmailStatsApp.attachement.file')" for="attachement-file">File</label>
            <div>
              <div v-if="attachement.file" class="form-text text-danger clearfix">
                <a class="pull-left" v-on:click="openFile(attachement.fileContentType, attachement.file)" v-text="$t('entity.action.open')"
                  >open</a
                ><br />
                <span class="pull-left">{{ attachement.fileContentType }}, {{ byteSize(attachement.file) }}</span>
                <button
                  type="button"
                  v-on:click="
                    attachement.file = null;
                    attachement.fileContentType = null;
                  "
                  class="btn btn-secondary btn-xs pull-right"
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                </button>
              </div>
              <input
                type="file"
                ref="file_file"
                id="file_file"
                data-cy="file"
                v-on:change="setFileData($event, attachement, 'file', false)"
                v-text="$t('entity.action.addblob')"
              />
            </div>
            <input
              type="hidden"
              class="form-control"
              name="file"
              id="attachement-file"
              data-cy="file"
              :class="{ valid: !$v.attachement.file.$invalid, invalid: $v.attachement.file.$invalid }"
              v-model="$v.attachement.file.$model"
            />
            <input
              type="hidden"
              class="form-control"
              name="fileContentType"
              id="attachement-fileContentType"
              v-model="attachement.fileContentType"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gmailStatsApp.attachement.message')" for="attachement-message">Message</label>
            <select class="form-control" id="attachement-message" data-cy="message" name="message" v-model="attachement.message">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="attachement.message && messageOption.id === attachement.message.id ? attachement.message : messageOption"
                v-for="messageOption in messages"
                :key="messageOption.id"
              >
                {{ messageOption.id }}
              </option>
            </select>
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
            :disabled="$v.attachement.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./attachement-update.component.ts"></script>
