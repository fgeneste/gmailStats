<template>
  <div>
    <h2 id="page-heading" data-cy="AttachementHeading">
      <span v-text="$t('gmailStatsApp.attachement.home.title')" id="attachement-heading">Attachements</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('gmailStatsApp.attachement.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'AttachementCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-attachement"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('gmailStatsApp.attachement.home.createLabel')"> Create a new Attachement </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && attachements && attachements.length === 0">
      <span v-text="$t('gmailStatsApp.attachement.home.notFound')">No attachements found</span>
    </div>
    <div class="table-responsive" v-if="attachements && attachements.length > 0">
      <table class="table table-striped" aria-describedby="attachements">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('gmailStatsApp.attachement.file')">File</span></th>
            <th scope="row"><span v-text="$t('gmailStatsApp.attachement.message')">Message</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="attachement in attachements" :key="attachement.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AttachementView', params: { attachementId: attachement.id } }">{{ attachement.id }}</router-link>
            </td>
            <td>
              <a
                v-if="attachement.file"
                v-on:click="openFile(attachement.fileContentType, attachement.file)"
                v-text="$t('entity.action.open')"
                >open</a
              >
              <span v-if="attachement.file">{{ attachement.fileContentType }}, {{ byteSize(attachement.file) }}</span>
            </td>
            <td>
              <div v-if="attachement.message">
                <router-link :to="{ name: 'MessageView', params: { messageId: attachement.message.id } }">{{
                  attachement.message.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'AttachementView', params: { attachementId: attachement.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'AttachementEdit', params: { attachementId: attachement.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(attachement)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="gmailStatsApp.attachement.delete.question" data-cy="attachementDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-attachement-heading" v-text="$t('gmailStatsApp.attachement.delete.question', { id: removeId })">
          Are you sure you want to delete this Attachement?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-attachement"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeAttachement()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./attachement.component.ts"></script>
