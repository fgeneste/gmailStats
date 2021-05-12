<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="attachement">
        <h2 class="jh-entity-heading" data-cy="attachementDetailsHeading">
          <span v-text="$t('gmailStatsApp.attachement.detail.title')">Attachement</span> {{ attachement.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('gmailStatsApp.attachement.file')">File</span>
          </dt>
          <dd>
            <div v-if="attachement.file">
              <a v-on:click="openFile(attachement.fileContentType, attachement.file)" v-text="$t('entity.action.open')">open</a>
              {{ attachement.fileContentType }}, {{ byteSize(attachement.file) }}
            </div>
          </dd>
          <dt>
            <span v-text="$t('gmailStatsApp.attachement.message')">Message</span>
          </dt>
          <dd>
            <div v-if="attachement.message">
              <router-link :to="{ name: 'MessageView', params: { messageId: attachement.message.id } }">{{
                attachement.message.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="attachement.id"
          :to="{ name: 'AttachementEdit', params: { attachementId: attachement.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./attachement-details.component.ts"></script>
