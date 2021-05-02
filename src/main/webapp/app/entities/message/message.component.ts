import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IMessage } from '@/shared/model/message.model';

import MessageService from './message.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Message extends Vue {
  @Inject('messageService') private messageService: () => MessageService;
  private removeId: number = null;

  public messages: IMessage[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllMessages();
  }

  public clear(): void {
    this.retrieveAllMessages();
  }

  public retrieveAllMessages(): void {
    this.isFetching = true;

    this.messageService()
      .retrieve()
      .then(
        res => {
          this.messages = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IMessage): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeMessage(): void {
    this.messageService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('gmailStatsApp.message.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllMessages();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
