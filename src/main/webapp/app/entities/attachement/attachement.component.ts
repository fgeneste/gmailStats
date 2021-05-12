import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IAttachement } from '@/shared/model/attachement.model';

import JhiDataUtils from '@/shared/data/data-utils.service';

import AttachementService from './attachement.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Attachement extends mixins(JhiDataUtils) {
  @Inject('attachementService') private attachementService: () => AttachementService;
  private removeId: number = null;

  public attachements: IAttachement[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllAttachements();
  }

  public clear(): void {
    this.retrieveAllAttachements();
  }

  public retrieveAllAttachements(): void {
    this.isFetching = true;

    this.attachementService()
      .retrieve()
      .then(
        res => {
          this.attachements = res.data;
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

  public prepareRemove(instance: IAttachement): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeAttachement(): void {
    this.attachementService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('gmailStatsApp.attachement.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllAttachements();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
