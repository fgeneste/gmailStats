<template>
  <div>
    <h2 id="page-heading" data-cy="MessageHeading">
      <span v-text="$t('gmailStatsApp.message.home.title')" id="message-heading">Messages</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('gmailStatsApp.message.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'MessageCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-message"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('gmailStatsApp.message.home.createLabel')"> Create a new Message </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && messages && messages.length === 0">
      <span v-text="$t('gmailStatsApp.message.home.notFound')">No messages found</span>
    </div>
    <div class="table-responsive" v-if="messages && messages.length > 0">
      <table class="table table-striped" aria-describedby="messages">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('account')">
              <span v-text="$t('gmailStatsApp.message.account')">Account</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'account'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('from')">
              <span v-text="$t('gmailStatsApp.message.from')">From</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'from'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('object')">
              <span v-text="$t('gmailStatsApp.message.object')">Object</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'object'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('corps')">
              <span v-text="$t('gmailStatsApp.message.corps')">Corps</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'corps'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('date')">
              <span v-text="$t('gmailStatsApp.message.date')">Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'date'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('stillOnServer')">
              <span v-text="$t('gmailStatsApp.message.stillOnServer')">Still On Server</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'stillOnServer'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="message in messages" :key="message.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'MessageView', params: { messageId: message.id } }">{{ message.id }}</router-link>
            </td>
            <td>{{ message.account }}</td>
            <td>{{ message.from }}</td>
            <td>{{ message.object }}</td>
            <td>{{ message.corps }}</td>
            <td>{{ message.date ? $d(Date.parse(message.date), 'short') : '' }}</td>
            <td>{{ message.stillOnServer }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'MessageView', params: { messageId: message.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'MessageEdit', params: { messageId: message.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(message)"
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
        <infinite-loading
          ref="infiniteLoading"
          v-if="totalItems > itemsPerPage"
          :identifier="infiniteId"
          slot="append"
          @infinite="loadMore"
          force-use-infinite-wrapper=".el-table__body-wrapper"
          :distance="20"
        >
        </infinite-loading>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="gmailStatsApp.message.delete.question" data-cy="messageDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-message-heading" v-text="$t('gmailStatsApp.message.delete.question', { id: removeId })">
          Are you sure you want to delete this Message?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-message"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeMessage()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./message.component.ts"></script>
